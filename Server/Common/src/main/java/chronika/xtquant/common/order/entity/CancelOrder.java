package chronika.xtquant.common.order.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CancelOrder {

    @Schema(description = "定单备注, 即客户端定单ID, 唯一性, 不可重复使用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String orderRemark;

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
