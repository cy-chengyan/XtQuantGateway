package chronika.xtquant.common.order.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Schema(description = "更新定单")
public class UpdateOrder {

    @Schema(description = "定单备注, 即客户端定单ID, 唯一性, 不可重复使用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String orderRemark;

    @Schema(description = "委托状态")
    private Integer orderStatus;

    @Schema(description = "错误信息(废单原因)")
    private String errorMsg;

    @Schema(description = "委托价格")
    private BigDecimal price;

    @Schema(description = "委托量")
    private Long orderVolume;

    @Schema(description = "已成量")
    private Long tradedVolume;

    @Schema(description = "已成交均价")
    private BigDecimal tradedPrice;

    @Schema(description = "已撤单数量")
    private Long canceledVolume;

    @Schema(description = "备注")
    private String memo;

    //
    // Getters and Setters
    //

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getOrderVolume() {
        return orderVolume;
    }

    public void setOrderVolume(Long orderVolume) {
        this.orderVolume = orderVolume;
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

    public Long getCanceledVolume() {
        return canceledVolume;
    }

    public void setCanceledVolume(Long canceledVolume) {
        this.canceledVolume = canceledVolume;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
