package chronika.xtquant.common.queue.entity;

import chronika.xtquant.common.infra.util.JsonUtil;
import chronika.xtquant.common.order.entity.Order;
import org.apache.commons.lang3.StringUtils;

public class OrderQueueMsg {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderQueueMsg.class);

    public static final int MSG_TYPE_PLACE_ORDER = 1;
    public static final int MSG_TYPE_CANCEL_ORDER = 2;

    private final int msgType;
    private final Order order;

    //
    // Constructor
    //

    public OrderQueueMsg(int msgType, Order order) {
        if (msgType < MSG_TYPE_PLACE_ORDER || msgType > MSG_TYPE_CANCEL_ORDER) {
            throw new IllegalArgumentException("Invalid msgType: " + msgType);
        }
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.msgType = msgType;
        this.order = order;
    }

    //
    // Getters
    //

    public int getMsgType() {
        return msgType;
    }

    public Order getOrder() {
        return order;
    }

    //
    // Others
    //

    public String serialize() {
        return msgType + "|" + JsonUtil.toJsonString(order);
    }

    public static OrderQueueMsg deserialize(String raw) {
        String[] parts = StringUtils.split(raw, '|');
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid OrderQueueMsg message: " + raw);
        }
        int msgType = Integer.parseInt(parts[0]);
        Order order = JsonUtil.jsonStringToObject(parts[1], Order.class);
        try {
            return new OrderQueueMsg(msgType, order);
        } catch (Exception e) {
            logger.error("Failed to deserialize OrderQueueMsg: {}", raw, e);
            return null;
        }
    }

    @Override
    public String toString() {
        return "OrderQueueMsg{" +
                "\"msgType\":" + (msgType == MSG_TYPE_PLACE_ORDER ? "\"MSG_TYPE_PLACE_ORDER\"" : "\"MSG_TYPE_CANCEL_ORDER\"") +
                ", \"order\":" + JsonUtil.toJsonString(order) +
                "}";
    }

}
