package chronika.xtquant.tradeapi.model.reqData;

import chronika.xtquant.common.order.entity.CancelOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CancelOrderReqData {

    @Schema(description = "撤单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Valid
    private CancelOrder order;

    //
    // Getters and Setters
    //

    public CancelOrder getOrder() {
        return order;
    }

    public void setOrder(CancelOrder order) {
        this.order = order;
    }

}
