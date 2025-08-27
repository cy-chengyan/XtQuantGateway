package chronika.xtquant.tradeapi.model.reqData;

import chronika.xtquant.common.order.entity.UpdateOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ManualUpdateOrderReqData {

    @Schema(description = "更新定单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private UpdateOrder updateOrder;

    //
    // Getters and Setters
    //

    public UpdateOrder getUpdateOrder() {
        return updateOrder;
    }

    public void setUpdateOrder(UpdateOrder updateOrder) {
        this.updateOrder = updateOrder;
    }

}
