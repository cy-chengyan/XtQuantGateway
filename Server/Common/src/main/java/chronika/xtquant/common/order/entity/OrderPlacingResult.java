package chronika.xtquant.common.order.entity;

public class OrderPlacingResult {

    public static int OrderPlacingResultSuccess = 1;
    public static int OrderPlacingResultFailed = 2;

    // 下单请求的定单备注, 即客户端定单id
    private String orderRemark;

    // 下单结果, 1成功, 2失败
    private Integer result;

    // 下单失败的原因
    private String reason;

    //
    // Constructors
    //

    public OrderPlacingResult() {
    }

    public OrderPlacingResult(String orderRemark, Integer result, String reason) {
        this.orderRemark = orderRemark;
        this.result = result;
        this.reason = reason;
    }

    //
    // Getters and Setters
    //

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    //
    // Others
    //

    public boolean isSuccess() {
        return result == OrderPlacingResultSuccess;
    }

    public static OrderPlacingResult success(String sid) {
        return new OrderPlacingResult(sid, OrderPlacingResultSuccess, null);
    }

    public static OrderPlacingResult failed(String sid, String reason) {
        return new OrderPlacingResult(sid, OrderPlacingResultFailed, reason);
    }

}
