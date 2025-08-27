package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;

public class ManualUpdateOrderResData {


    @Schema(description = "更新后的定单")
    private Order order;

    //
    // Constructors
    //
    public ManualUpdateOrderResData() {
    }

    public ManualUpdateOrderResData(Order order) {
        this.order = order;
    }

    //
    // Getters and Setters
    //
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    //
    // Builder
    //
    public static ManualUpdateOrderResDataBuilder builder() {
        return new ManualUpdateOrderResDataBuilder();
    }

    public static class ManualUpdateOrderResDataBuilder {
        private Order order;

        public ManualUpdateOrderResDataBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public ManualUpdateOrderResData build() {
            return new ManualUpdateOrderResData(order);
        }
    }

}
