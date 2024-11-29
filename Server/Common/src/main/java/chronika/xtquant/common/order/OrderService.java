package chronika.xtquant.common.order;

import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.common.infra.exception.CkException;
import chronika.xtquant.common.infra.util.DateUtil;
import chronika.xtquant.common.order.entity.CancelOrder;
import chronika.xtquant.common.order.entity.NewOrder;
import chronika.xtquant.common.order.entity.Order;
import chronika.xtquant.common.order.entity.OrderPlacingResult;
import chronika.xtquant.common.order.repo.OrderRepo;
import chronika.xtquant.common.queue.OrderQueueService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);

    private final OrderRepo orderRepo;
    private final OrderQueueService orderQueueService;

    @Autowired
    public OrderService(OrderRepo orderRepo, OrderQueueService orderQueueService) {
        this.orderRepo = orderRepo;
        this.orderQueueService = orderQueueService;
    }

    public Order replace(Order order) {
        Order ret;
        Order existed = orderRepo.findByRemark(order.getOrderRemark());
        if (existed != null) { // 如果定单存在
            if (existed.getManualUpdatedAt() != null) { // 如果定单是手动更新的, 则不更新
                ret = existed;
            } else { // 如果定单不是手动更新的, 则更新
                order.setId(existed.getId());
                if (!order.equals(existed)) { // 定单有变化才更新
                    log.info("[定单更新]:{} >>> {}", existed, order);
                    orderRepo.save(order);
                }
                ret = order;
            }
        } else { // 如果定单不存在, 则保存
            log.info("[定单新增]:{}", order);
            ret = orderRepo.save(order);
        }
        return ret;
    }

    public List<Order> queryCurrentOrders(String accountId, List<Integer> status) {
        int today = DateUtil.currentYmd();
        if (CollectionUtils.isEmpty(status)) {
            return orderRepo.findOnDay(accountId, today);
        } else {
            return orderRepo.findOnDayByStatus(accountId, today, status);
        }
    }

    public boolean existsByOrderRemark(String orderRemark) {
        return orderRepo.existsByRemark(orderRemark);
    }

    public OrderPlacingResult asyncPlaceOrder(NewOrder order) {
        List<NewOrder> orders = List.of(order);
        List<OrderPlacingResult> results = this.asyncPlaceOrders(orders);
        return results.get(0);
    }

    public List<OrderPlacingResult> asyncPlaceOrders(List<NewOrder> orders) {
        // 如果 orders 里存在相同的 orderRemark, 则直接返回失败
        boolean hasDuplicateSid = orders.stream()
            .map(NewOrder::getOrderRemark)
            .collect(Collectors.toSet())
            .size() != orders.size();
        if (hasDuplicateSid) {
            throw new CkException(CkError.INVALID_PARAM, "Duplicate orderRemark in orders");
        }

        // 先过滤掉已存在的定单
        List<NewOrder> notExistsOrders = Lists.newArrayList();
        Map<String, OrderPlacingResult> failedResults = Maps.newHashMap();
        for (NewOrder order : orders) {
            if (existsByOrderRemark(order.getOrderRemark())) {
                failedResults.put(order.getOrderRemark(), OrderPlacingResult.failed(order.getOrderRemark(), "Order already exists"));
            } else {
                notExistsOrders.add(order);
            }
        }

        if (CollectionUtils.isEmpty(notExistsOrders)) {
            return orders.stream()
                .map(order -> failedResults.getOrDefault(order.getOrderRemark(), OrderPlacingResult.failed(order.getOrderRemark(), "Unknown error")))
                .collect(Collectors.toList());
        }

        // 定单保存
        Map<String, OrderPlacingResult> newOrderResults = new HashMap<>(notExistsOrders.size());
        List<Order> saveOrders = Order.createByNewOrders(notExistsOrders);
        saveOrders.forEach(order -> {
            String orderRemark = order.getOrderRemark();
            OrderPlacingResult result;
            try {
                log.info("[定单新增]:{}", order);
                order = orderRepo.save(order);
            } catch (Exception e) {
                log.error("Failed to save order: {}", orderRemark, e);
                result = OrderPlacingResult.failed(orderRemark, "Failed to save order");
                newOrderResults.put(orderRemark, result);
                return;
            }

            if (!orderQueueService.sendNewOrder(order)) {
                // 如果发送到队列失败, 则将定单状态置为废单
                order.setOrderStatus(Order.ORDER_STATUS_JUNK);
                order.setErrorMsg("发送定单到消息队列失败");
                try {
                    log.info("[发送新定单到队列失败, 转而变废单]:{}", order);
                    orderRepo.save(order);
                } catch (Exception e) {
                    log.error("[严重错误]向消息队列发送新定单失败后, 修改定单状态失败, {}", order, e);
                }
                result = OrderPlacingResult.failed(orderRemark, "Failed to send order to queue");
            } else {
                result = OrderPlacingResult.success(orderRemark);
            }
            newOrderResults.put(orderRemark, result);
        });

        // 按照请求的顺序返回结果
        List<OrderPlacingResult> results = Lists.newArrayList();
        for (NewOrder order : orders) {
            String orderRemark = order.getOrderRemark();
            OrderPlacingResult result = failedResults.get(orderRemark);
            if (result == null) {
                result = newOrderResults.get(orderRemark);
            }
            results.add(result);
        }

        return results;
    }

    public OrderPlacingResult asyncCancelOrder(CancelOrder order) {
        List<CancelOrder> orders = List.of(order);
        List<OrderPlacingResult> results = this.asyncCancelOrders(orders);
        return results.get(0);
    }

    public List<OrderPlacingResult> asyncCancelOrders(List<CancelOrder> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return List.of();
        }

        Map<String, OrderPlacingResult> cancelOrderResults = new HashMap<>(orders.size());
        for (CancelOrder order : orders) {
            OrderPlacingResult result;
            String orderRemark = order.getOrderRemark();
            Order existed = orderRepo.findByRemark(orderRemark);
            if (existed == null) {
                result = OrderPlacingResult.failed(orderRemark, "Order not exists");
                cancelOrderResults.put(orderRemark, result);
                continue;
            }

            if (existed.getManualUpdatedAt() != null) { // 手动修改过的定单不允许做任何变动
                result = OrderPlacingResult.failed(orderRemark, "Order is manual updated");
                cancelOrderResults.put(orderRemark, result);
                continue;
            }

            /* 以下逻辑直接放在 OrderPlacer 中处理, 这样可以保证只在 OrderPlacer 模块中队列串行处理, 避免并发问题
            // 如果状态是本地保存状态, 则直接修改状态
            if (existed.getOrderStatus() == Order.ORDER_STATUS_LOCAL_SAVED) {
                existed.setOrderStatus(Order.ORDER_STATUS_CANCELED);
                try {
                    orderRepo.save(existed);
                    result = OrderPlacingResult.success(orderRemark);
                } catch (Exception e) {
                    log.error("Failed to cancel order(ORDER_STATUS_LOCAL): {}", order, e);
                    result = OrderPlacingResult.failed(orderRemark, "Failed to cancel order(ORDER_STATUS_LOCAL)");
                }
                cancelOrderResults.put(orderRemark, result);
                continue;
            }*/

            // 其它状态的定单, 往消息队列发送撤单消息
            if (!orderQueueService.sendCancelOrder(existed)) {
                result = OrderPlacingResult.failed(orderRemark, "Failed to send cancel order to queue");
            } else {
                result = OrderPlacingResult.success(orderRemark);
            }
            cancelOrderResults.put(orderRemark, result);
        }

        // 按照请求的顺序返回结果
        List<OrderPlacingResult> results = Lists.newArrayList();
        for (CancelOrder order : orders) {
            OrderPlacingResult result = cancelOrderResults.get(order.getOrderRemark());
            results.add(result);
        }
        return results;
    }

}
