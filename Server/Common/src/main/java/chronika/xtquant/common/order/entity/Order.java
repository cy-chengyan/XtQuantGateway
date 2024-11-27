package chronika.xtquant.common.order.entity;

import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.infra.misc.Constants;
import chronika.xtquant.common.infra.util.DateUtil;
import chronika.xtquant.common.infra.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "t_order")
public class Order {

    public static final int ORDER_TYPE_STOCK_BUY = 23; // 证券买入
    public static final int ORDER_TYPE_STOCK_SELL = 24; // 证券卖出

    public static final int PRICE_TYPE_LATEST = 5;
    public static final int PRICE_TYPE_LIMIT = 11;

    public static final int ORDER_STATUS_LOCAL = -1; // 保存在本地
    public static final int ORDER_STATUS_LOCAL_SUBMITTING = -2; // 本地提交中(正在往miniQMT提交)
    public static final int ORDER_STATUS_LOCAL_CANCELING = -3; // 本地撤单中(正在往miniQMT提交)

    public static final int ORDER_STATUS_UNKNOWN = 255; // 未知
    public static final int ORDER_STATUS_UNREPORTED = 48; // 未报
    public static final int ORDER_STATUS_WAIT_REPORTING = 49; // 待报
    public static final int ORDER_STATUS_REPORTED = 50; // 已报
    public static final int ORDER_STATUS_REPORTED_CANCEL = 51; // 已报待撤
    public static final int ORDER_STATUS_PARTSUCC_CANCEL = 52; // 部成待撤
    public static final int ORDER_STATUS_PART_CANCEL = 53; // 部撤
    public static final int ORDER_STATUS_CANCELED = 54; // 已撤
    public static final int ORDER_STATUS_PART_SUCC = 55; // 部成
    public static final int ORDER_STATUS_SUCCEEDED = 56; // 已成
    public static final int ORDER_STATUS_JUNK = 57; // 废单


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Order.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Schema(description = "定单备注(实为下游调用者设定的定单id, 具有唯一性)")
    private String orderRemark;

    @Schema(description = "账号ID")
    private String accountId;

    @Schema(description = "日期, format: YYYYMMDD")
    private int date;

    @Schema(description = "股票代码")
    private String stockCode;

    @Schema(description = "订单编号")
    private Long orderId;

    @Schema(description = "委托编号(柜台合同编号)")
    private String orderSysId;

    @Schema(description = "报单时间, format: HHMMSS")
    private int orderTime;

    @Schema(description = "定单类型")
    private int orderType;

    @Schema(description = "定单数量")
    private Long orderVolume;

    @Schema(description = "定单价格类型")
    private int priceType;

    @Schema(description = "定单价格")
    private BigDecimal price;

    @Schema(description = "成交数量")
    private Long tradedVolume;

    @Schema(description = "成交均价")
    private BigDecimal tradedPrice;

    @Schema(description = "定单状态")
    private int orderStatus;

    @Schema(description = "已撤单数量")
    private Long canceledVolume;

    @Schema(description = "策略名称")
    private String strategyName;

    @Schema(description = "错误信息(废单原因)")
    private String errorMsg;

    @Schema(description = "记录创建时间戳(秒)")
    @Column(name = "`created_at`", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Schema(description = "记录更新时间戳(秒)")
    @Column(name = "`updated_at`", insertable = false, updatable = false)
    private Timestamp updatedAt;

    @Schema(description = "手动更新时间(毫秒)")
    private Long manualUpdatedAt;

    @Schema(description = "备注")
    private String memo;

    //
    // getters and setters
    //


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSysId() {
        return orderSysId;
    }

    public void setOrderSysId(String orderSysId) {
        this.orderSysId = orderSysId;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public Long getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(Long orderVolume) {
        this.orderVolume = orderVolume;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getTradedVolume() {
        return tradedVolume;
    }

    public void setTradedVolume(Long tradedVolume) {
        this.tradedVolume = tradedVolume;
    }

    public BigDecimal getTradedPrice() {
        return tradedPrice;
    }

    public void setTradedPrice(BigDecimal tradedPrice) {
        this.tradedPrice = tradedPrice;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getCanceledVolume() {
        return canceledVolume;
    }

    public void setCanceledVolume(Long canceledVolume) {
        this.canceledVolume = canceledVolume;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getManualUpdatedAt() {
        return manualUpdatedAt;
    }

    public void setManualUpdatedAt(Long manualUpdatedAt) {
        this.manualUpdatedAt = manualUpdatedAt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    //
    // Other methods
    //

    @Override
    public String toString() {
        return "Order:" + JsonUtil.toJsonString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Asset other)) {
            return false;
        }
        return this.checkSum().equals(other.checkSum());
    }

    public String checkSum () {
        return orderRemark + ";"
            + orderStatus + ";"
            + tradedVolume + ";"
            + tradedPrice + ";"
            + canceledVolume;
    }

    //
    // Constructors
    //

    public Order() {
    }

    public Order(String[] feedLineFields) {
        this.accountId = StringUtils.split(feedLineFields[0], "____")[4];
        this.stockCode = feedLineFields[2] + "." + feedLineFields[1];
        this.orderType = feedLineFields[3].contains("买") ? ORDER_TYPE_STOCK_BUY : ORDER_TYPE_STOCK_SELL;
        this.priceType = feedLineFields[4].contains("限价") ? PRICE_TYPE_LIMIT : PRICE_TYPE_LATEST;
        this.price = new BigDecimal(feedLineFields[5]).setScale(Constants.PriceDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.orderVolume = Long.parseLong(feedLineFields[6]);
        this.date = Integer.parseInt(feedLineFields[7]);
        this.orderTime = Integer.parseInt(feedLineFields[8]);
        this.orderSysId = feedLineFields[9];
        this.orderStatus = parseOrderStatus(feedLineFields[10]);
        this.tradedVolume = Long.parseLong(feedLineFields[11]);
        this.tradedPrice = new BigDecimal(feedLineFields[12]).setScale(Constants.PriceDecimalPrecision, Constants.FinDecimalRoundingMode);
        this.canceledVolume = Long.parseLong(feedLineFields[14]);
        this.orderId = feedLineFields[15].isEmpty() ? null : Long.parseLong(feedLineFields[15]);
        this.strategyName = feedLineFields[16].isEmpty() ? null : feedLineFields[16];
        this.orderRemark = feedLineFields[17].isEmpty() ? null : feedLineFields[17];
        this.errorMsg = feedLineFields[18].isEmpty() ? null : feedLineFields[18];
    }

    public static Order createByFeedLine(String[] lineFields) {
        if (lineFields == null || lineFields.length < 20) {
            return null;
        }
        try {
            return new Order(lineFields);
        } catch (Exception e) {
            log.error("Failed to create Order by fields: {}", e.getMessage());
            return null;
        }
    }

    public static Order createByNewOrder(NewOrder newOrder) {
        Order order = new Order();
        order.orderRemark = newOrder.getOrderRemark();
        order.accountId = newOrder.getAccountId();
        order.date = DateUtil.currentYmd();
        order.stockCode = newOrder.getStockCode();
        order.orderType = newOrder.getOrderType();
        order.priceType = newOrder.getPriceType();
        order.orderStatus = Order.ORDER_STATUS_LOCAL;
        order.price = newOrder.getPrice();
        order.orderVolume = newOrder.getOrderVolume();
        order.memo = newOrder.getMemo();

        order.orderTime = DateUtil.currentHms();
        order.tradedVolume = 0L;
        order.tradedPrice = BigDecimal.ZERO;
        order.canceledVolume = 0L;

        return order;
    }

    public static List<Order> createByNewOrders(List<NewOrder> newOrders) {
        return newOrders.stream()
            .map(Order::createByNewOrder)
            .collect(Collectors.toList());
    }

    private int parseOrderStatus(String status) {
        if (status.contains("未报")) {
            return ORDER_STATUS_UNREPORTED;
        } else if (status.contains("待报")) {
            return ORDER_STATUS_WAIT_REPORTING;
        } else if (status.contains("已报")) {
            return ORDER_STATUS_REPORTED;
        } else if (status.contains("已报待撤")) {
            return ORDER_STATUS_REPORTED_CANCEL;
        } else if (status.contains("部成待撤")) {
            return ORDER_STATUS_PARTSUCC_CANCEL;
        } else if (status.contains("部撤")) {
            return ORDER_STATUS_PART_CANCEL;
        } else if (status.contains("已撤")) {
            return ORDER_STATUS_CANCELED;
        } else if (status.contains("部成")) {
            return ORDER_STATUS_PART_SUCC;
        } else if (status.contains("已成")) {
            return ORDER_STATUS_SUCCEEDED;
        } else if (status.contains("废单")) {
            return ORDER_STATUS_JUNK;
        } else {
            return ORDER_STATUS_UNKNOWN;
        }
    }

}
