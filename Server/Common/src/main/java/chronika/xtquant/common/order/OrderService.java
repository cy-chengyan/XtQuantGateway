package chronika.xtquant.common.order;

import chronika.xtquant.common.infra.enums.CkError;
import chronika.xtquant.common.infra.exception.CkException;
import chronika.xtquant.common.infra.util.DateUtil;
import chronika.xtquant.common.order.entity.*;
import chronika.xtquant.common.order.repo.OrderRepo;
import chronika.xtquant.common.queue.OrderQueueService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    public List<Order> queryCurrentOrders(String accountId, List<Integer> orderStatus) {
        int today = DateUtil.currentYmd();
        if (CollectionUtils.isEmpty(orderStatus)) {
            return orderRepo.findOnDay(accountId, today);
        } else {
            return orderRepo.findOnDayByStatus(accountId, today, orderStatus);
        }
    }

    public Page<Order> queryOrders(String accountId, String orderRemark, Integer date, List<Integer> orderStatus, String stockCode, Pageable pageable) {
        if (!StringUtils.hasLength(accountId)) {
            accountId = null;
        }
        if (!StringUtils.hasLength(orderRemark)) {
            orderRemark = null;
        }
        if (CollectionUtils.isEmpty(orderStatus)) {
            orderStatus = null;
        }
        if (!StringUtils.hasLength(stockCode)) {
            stockCode = null;
        }
        return orderRepo.findAll(accountId, orderRemark, date, orderStatus, stockCode, pageable);
    }

    public Order queryByOrderRemark(String orderRemark) {
        return orderRepo.findByRemark(orderRemark);
    }

    public boolean existsByOrderRemark(String orderRemark) {
        return orderRepo.existsByRemark(orderRemark);
    }

    public boolean updateOrderStatus(String orderRemark, int orderStatus) {
        try {
            orderRepo.updateStatusByRemark(orderRemark, orderStatus);
            return true;
        } catch (Exception e) {
            log.error("Failed to update order status: orderRemark={}, to orderStatus={}", orderRemark, orderStatus, e);
            return false;
        }
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

    public Order manualUpdateOrder(UpdateOrder updateOrder) {
        // 查找定单
        String orderRemark = updateOrder.getOrderRemark();
        Order order = orderRepo.findByRemark(orderRemark);
        if (order == null) {
            throw new CkException(CkError.RECORD_NOT_FOUND, "Order " + orderRemark + "(remark) not found");
        }

        boolean needUpdate = false;
        Integer status = updateOrder.getOrderStatus();
        if (status != null && status.compareTo(order.getOrderStatus()) != 0) {
            order.setOrderStatus(status);
            needUpdate = true;
        }

        String errorMsg = updateOrder.getErrorMsg();
        if (errorMsg != null
            && (order.getErrorMsg() == null || !errorMsg.equals(order.getErrorMsg()))) {
            order.setErrorMsg(errorMsg);
            needUpdate = true;
        }

        BigDecimal price = updateOrder.getPrice();
        if (price != null && price.compareTo(order.getPrice()) != 0) {
            order.setPrice(price);
            needUpdate = true;
        }

        Long volume = updateOrder.getOrderVolume();
        if (volume != null && volume.compareTo(order.getOrderVolume()) != 0) {
            order.setOrderVolume(volume);
            needUpdate = true;
        }

        Long tradedVolume = updateOrder.getTradedVolume();
        if (tradedVolume != null
            && (order.getTradedVolume() == null || tradedVolume.compareTo(order.getTradedVolume()) != 0)) {
            order.setTradedVolume(tradedVolume);
            needUpdate = true;
        }

        BigDecimal tradedPrice = updateOrder.getTradedPrice();
        if (tradedPrice != null
            && (order.getTradedPrice() == null || tradedPrice.compareTo(order.getTradedPrice()) != 0)) {
            order.setTradedPrice(tradedPrice);
            needUpdate = true;
        }

        String memo = updateOrder.getMemo();
        if (memo != null
            && (order.getMemo() == null || !memo.equals(order.getMemo()))) {
            order.setMemo(memo);
            needUpdate = true;
        }

        Order ret;
        if (needUpdate) {
            Long now = DateUtil.currentMillSecond();
            order.setManualUpdatedAt(now);
            ret = orderRepo.save(order);
        } else {
            ret = order;
        }
        return ret;
    }

}
