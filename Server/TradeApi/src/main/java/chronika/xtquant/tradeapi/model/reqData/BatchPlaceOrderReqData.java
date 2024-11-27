package chronika.xtquant.tradeapi.model.reqData;

import chronika.xtquant.common.order.entity.NewOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BatchPlaceOrderReqData {

    @Schema(description = "定单列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @Valid
    private List<@NotNull NewOrder> orders;

    //
    // Getters and Setters
    //

    public List<NewOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<NewOrder> orders) {
        this.orders = orders;
    }

}
