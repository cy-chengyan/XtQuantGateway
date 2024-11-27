package chronika.xtquant.common.order.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class NewOrder {

    @Schema(description = "定单备注, 即客户端定单ID, 唯一性, 不可重复使用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String orderRemark;

    @Schema(description = "交易账号ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String accountId;

    @Schema(description = "标的代码, 格式: 代码.市场 例如:平安银行 000001.SZ 浦发银行 600000.SH", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String stockCode;

    @Schema(description = "委托量, 整数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long orderVolume;

    @Schema(description = "委托类型, 23: 买入, 24: 卖出; 参见: http://docs.thinktrader.net/pages/198696/#%E5%A7%94%E6%89%98%E7%B1%BB%E5%9E%8B-order-type", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer orderType;

    @Schema(description = "委托价格类型, 5: 最新价, 11: 限价; 参见: http://docs.thinktrader.net/pages/198696/#%E6%8A%A5%E4%BB%B7%E7%B1%BB%E5%9E%8B-price-type", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer priceType;

    @Schema(description = "限价委托的委托价格, 市价委托的保护价", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal price;

    @Schema(description = "备注说明, 不需要时可给空字符串", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Long getOrderVolume() {
        return orderVolume;
    }

    public void setVolume(Long orderVolume) {
        this.orderVolume = orderVolume;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

}
