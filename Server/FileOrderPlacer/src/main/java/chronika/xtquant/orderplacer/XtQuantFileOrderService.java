package chronika.xtquant.orderplacer;

import chronika.xtquant.common.infra.exception.CkException;
import chronika.xtquant.common.infra.util.DateUtil;
import chronika.xtquant.common.order.OrderService;
import chronika.xtquant.common.order.entity.Order;
import chronika.xtquant.common.order.entity.OrderPlacingResult;
import chronika.xtquant.common.queue.entity.OrderQueueMsg;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class XtQuantFileOrderService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(XtQuantFileOrderService.class);

    private static final String orderFilePrefix = "signal.";
    private static final String accountType = "2"; // 资金账号类型, 由于目前不支持其它类型的账号, 所以恒为 2-股票

    private final String orderDirPath;
    private final String orderTmpDirPath;

    private final OrderService orderService;

    public XtQuantFileOrderService(@Value("${xtquant.order-dir}") String orderDirPath,
                                   @Value("${xtquant.order-tmp-dir}") String orderTmpDirPath,
                                   OrderService orderService) {
        this.orderDirPath = orderDirPath;
        if (!StringUtils.hasLength(orderDirPath)) {
            throw new CkException("'xtquant.order-dir' is not set");
        }
        this.orderTmpDirPath = orderTmpDirPath;
        if (!StringUtils.hasLength(orderTmpDirPath)) {
            throw new CkException("'xtquant.order-tmp-dir' is not set");
        }
        this.orderService = orderService;
    }

    public void processOrderQueueMsg(OrderQueueMsg msg) {
        log.info("收到订单队列消息: {}", msg);
        switch (msg.getMsgType()) {
            case OrderQueueMsg.MSG_TYPE_PLACE_ORDER:
                processNewOrder(msg.getOrder());
                break;
            case OrderQueueMsg.MSG_TYPE_CANCEL_ORDER:
                processCancelOrder(msg.getOrder());
                break;
            default:
                log.error("未知的订单队列消息类型: {}", msg);
        }
    }

    private void processNewOrder(Order o) {
        log.info("[下单]开始处理新定单:{}", o);
        String orderRemark = o.getOrderRemark();
        Order order = orderService.queryByOrderRemark(orderRemark);
        if (order == null) {
            log.error("[下单]找不到定单, orderRemark:{}", orderRemark);
            return;
        }

        int orderStatus = order.getOrderStatus();
        if (orderStatus != Order.ORDER_STATUS_LOCAL_SAVED) {
            log.error("[下单]当前定单{}状态为:{}, 不能下单", orderRemark, orderStatus);
            return;
        }

        log.info("[下单]开始提交文件单, 将定单状态更新为:本地提交中");
        if (!orderService.updateOrderStatus(orderRemark, Order.ORDER_STATUS_LOCAL_SUBMITTING)) {
            log.error("[下单]更新定单状态({}->{})失败, orderRemark:{}",
                orderStatus, Order.ORDER_STATUS_LOCAL_SUBMITTING, orderRemark);
            return;
        }

        List<Order> orders = List.of(order);
        List<OrderPlacingResult> results = filePlaceOrders(orders);
        if (CollectionUtils.isEmpty(results)) {
            log.error("[下单]下单失败, 未返回结果, orderRemark:{}", orderRemark);
            return;
        }

        OrderPlacingResult result = results.get(0);
        if (result.isSuccess()) {
            log.info("[下单]生成文件单成功, orderRemark:{}", orderRemark);
        } else {
            log.error("[下单]生成文件单失败, orderRemark:{}, 原因:{}", orderRemark, result.getReason());
            orderService.updateOrderStatus(orderRemark, Order.ORDER_STATUS_LOCAL_SUBMIT_FAILED);
        }
    }

    private void processCancelOrder(Order o) {
        log.info("[撤单]开始处理撤单:{}", o);
        String orderRemark = o.getOrderRemark();
        Order order = orderService.queryByOrderRemark(orderRemark);
        if (order == null) {
            log.error("[撤单]找不到定单, orderRemark:{}", orderRemark);
            return;
        }

        int orderStatus = order.getOrderStatus();
        if (orderStatus == Order.ORDER_STATUS_SUCCEEDED) {
            log.error("[撤单]当前定单{}状态为已成, 不能撤单", orderRemark);
            return;
        } else if (orderStatus == Order.ORDER_STATUS_CANCELED) {
            log.error("[撤单]当前定单{}状态为已撤, 不能再次撤单", orderRemark);
            return;
        } else if (orderStatus == Order.ORDER_STATUS_JUNK) {
            log.error("[撤单]当前定单{}状态为废单, 不能撤单", orderRemark);
            return;
        } else if (orderStatus == Order.ORDER_STATUS_LOCAL_SUBMIT_CANCELING) {
            log.error("[撤单]当前定单{}状态为本地正在向QMT提交撤单中, 不能再次撤单", orderRemark);
            return;
        }

        if (orderStatus == Order.ORDER_STATUS_LOCAL_SAVED || orderStatus == Order.ORDER_STATUS_LOCAL_SUBMIT_FAILED) {
            log.info("[撤单]当前定单{}状态为本地保存或提交失败, 直接修改状态为已撤", orderRemark);
            orderService.updateOrderStatus(orderRemark, Order.ORDER_STATUS_CANCELED);
            return;
        }

        Long orderId = order.getOrderId();
        if (orderId == null || orderId <= 0) {
            log.error("[撤单]当前定单{}委托编号(orderId字段)不是有效值:{}, 不能撤单", orderRemark, orderId);
            return;
        }

        log.info("[撤单]开始提交文件单, 将定单状态更新为:本地提交撤单中");
        if (!orderService.updateOrderStatus(orderRemark, Order.ORDER_STATUS_LOCAL_SUBMIT_CANCELING)) {
            log.error("[撤单]更新定单状态({}->{})失败, orderRemark:{}",
                orderStatus, Order.ORDER_STATUS_LOCAL_SUBMIT_CANCELING, orderRemark);
            return;
        }

        List<Order> orders = List.of(order);
        List<OrderPlacingResult> results = fileCancelOrders(orders);
        if (CollectionUtils.isEmpty(results)) {
            log.error("[撤单]撤单失败, 未返回结果, orderRemark:{}", orderRemark);
            return;
        }

        OrderPlacingResult result = results.get(0);
        if (result.isSuccess()) {
            log.info("[撤单]生成撤单文件单成功, orderRemark:{}", orderRemark);
        } else {
            log.error("[撤单]生成撤单文件单失败, orderRemark:{}, 原因:{}", orderRemark, result.getReason());
        }
    }

    private String generalOrderFileName(List<Order> orders, boolean isCancelOrder) {
        Order firstOrder = orders.get(0);
        String fileNo;
        if (isCancelOrder) {
            // 撤单文件序号为 cancel-原始订单号-时间戳(精确到毫秒)
            fileNo = "cancel-" + firstOrder.getOrderRemark() + "-" + DateUtil.currentDatetimeStr("HHmmss-SSS");
        } else {
            fileNo = firstOrder.getOrderRemark(); // 将第一个定单的本地定单号当做文件序号
        }
        String accountId = firstOrder.getAccountId();
        return orderFilePrefix + accountId + "_" + accountType + "." + fileNo + ".txt";
    }

    private boolean moveOrderFileToDir(String fileTmpPath, String fileDestPath) {
        try {
            log.info("[下单/撤单]移动文件单: {} -> {}", fileTmpPath, fileDestPath);
            Files.move(Paths.get(fileTmpPath), Paths.get(fileDestPath));
            return true;
        } catch (Exception e) {
            log.error("[下单/撤单]移动文件单失败: {} -> {}", fileTmpPath, fileDestPath, e);
            return false;
        }
    }

    private List<OrderPlacingResult> filePlaceOrders(List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return List.of();
        }

        // 在 orderDirPath 目录下创建一个 signal.资金账号_账号类型.下单文件序号.txt 文件
        // 注意, 由于QMT终端会实时扫描 txt 文件并删除并备份, 所以生成的文件要先写入到 orderTmpDirPath 后再移动到 orderDirPath 目录
        String fileName = generalOrderFileName(orders, false);
        String fileTmpPath = orderTmpDirPath + "/" + fileName;
        FileWriter writer;
        try {
            log.info("[下单]创建文件单: {}", fileTmpPath);
            writer = new FileWriter(fileTmpPath, StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            log.error("[下单]创建文件单失败, 创建文件失败: {}", fileTmpPath, e);
            return orders.stream()
                .map(o -> OrderPlacingResult.failed(o.getOrderRemark(), "Create and write file failed"))
                .toList();
        }

        List<OrderPlacingResult> results = new ArrayList<>();

        for (Order o : orders) {
            String sid = o.getOrderRemark();
            log.info("[下单]写入订单到文件单: {}", o);
            try {
                writer.write(o.toOrderPlacingFileLine());
                results.add(OrderPlacingResult.success(sid));
            } catch (Exception e) {
                log.error("[下单]写入订单到文件单失败: {}", o, e);
                results.add(OrderPlacingResult.failed(sid, "Write order to template file failed"));
            }
        }

        try {
            writer.close();
        } catch (Exception e) {
            log.error("[下单]关闭文件单失败: {}", fileTmpPath, e);
        }

        // touch 结束标识文件
        String finFileTmpPath = fileTmpPath + ".fin";
        try {
            FileUtils.touch(new File(finFileTmpPath));
        } catch (Exception e) {
            log.error("[下单]创建结束标识文件失败: {}", finFileTmpPath, e);
            return orders.stream()
                .map(o -> OrderPlacingResult.failed(o.getOrderRemark(), "Create finish file failed"))
                .toList();
        }

        // 移动文件到 orderDirPath 目录
        String fileDestPath = orderDirPath + "/" + fileName;
        String finFileDestPath = fileDestPath + ".fin";
        if (!moveOrderFileToDir(fileTmpPath, fileDestPath)
            || !moveOrderFileToDir(finFileTmpPath, finFileDestPath)) {
            return orders.stream()
                .map(o -> OrderPlacingResult.failed(o.getOrderRemark(), "Move order's file failed"))
                .toList();
        }

        return results;
    }

    public List<OrderPlacingResult> fileCancelOrders(List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return List.of();
        }

        // 在 orderDirPath 目录下创建一个 signal.资金账号_账号类型.下单文件序号.txt 文件
        // 注意, 由于QMT终端会实时扫描 txt 文件并删除并备份, 所以生成的文件要先写入到 orderTmpDirPath 后再移动到 orderDirPath 目录
        String fileName = generalOrderFileName(orders, true);
        String fileTmpPath = orderTmpDirPath + "/" + fileName;
        FileWriter writer;
        try {
            log.info("[撤单]创建文件单: {}", fileTmpPath);
            writer = new FileWriter(fileTmpPath, StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            log.error("[撤单]创建文件单失败, 创建文件失败: {}", fileTmpPath, e);
            return orders.stream()
                .map(o -> OrderPlacingResult.failed(o.getOrderRemark(), "Create and write file failed"))
                .toList();
        }

        List<OrderPlacingResult> results = new ArrayList<>();

        for (Order o : orders) {
            String sid = o.getOrderRemark();
            log.info("[撤单]写入订单到文件单: {}", o);
            try {
                writer.write(o.toOrderCancelFileLine());
                results.add(OrderPlacingResult.success(sid));
            } catch (Exception e) {
                log.error("[撤单]写入订单到文件单失败: {}", o, e);
                results.add(OrderPlacingResult.failed(sid, "Write order to template file failed"));
            }
        }

        try {
            writer.close();
        } catch (Exception e) {
            log.error("[撤单]关闭文件单失败: {}", fileTmpPath, e);
        }

        // touch 结束标识文件
        String finFileTmpPath = fileTmpPath + ".fin";
        try {
            FileUtils.touch(new File(finFileTmpPath));
        } catch (Exception e) {
            log.error("[撤单]创建结束标识文件失败: {}", finFileTmpPath, e);
            return orders.stream()
                .map(o -> OrderPlacingResult.failed(o.getOrderRemark(), "Create finish file failed"))
                .toList();
        }

        // 移动文件到 orderDirPath 目录
        String fileDestPath = orderDirPath + "/" + fileName;
        String finFileDestPath = fileDestPath + ".fin";
        if (!moveOrderFileToDir(fileTmpPath, fileDestPath)
            || !moveOrderFileToDir(finFileTmpPath, finFileDestPath)) {
            return orders.stream()
                .map(o -> OrderPlacingResult.failed(o.getOrderRemark(), "Move order's file failed"))
                .toList();
        }

        return results;
    }

}
