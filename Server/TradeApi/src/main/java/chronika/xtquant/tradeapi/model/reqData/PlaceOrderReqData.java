package chronika.xtquant.tradeapi.model.reqData;

import chronika.xtquant.common.order.entity.NewOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class PlaceOrderReqData {

    @Schema(description = "定单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Valid
    private NewOrder order;

    //
    // Getters and Setters
    //

    public NewOrder getOrder() {
        return order;
    }

    public void setOrder(NewOrder order) {
        this.order = order;
    }

}
