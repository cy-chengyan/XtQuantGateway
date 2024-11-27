package chronika.xtquant.tradeapi.model.reqData;


import chronika.xtquant.common.order.entity.CancelOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BatchCancelOrderReqData {

    @Schema(description = "撤单列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @Valid
    private List<@NotNull CancelOrder> orders;

    //
    // Getters and Setters
    //

    public List<CancelOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CancelOrder> orders) {
        this.orders = orders;
    }

}
