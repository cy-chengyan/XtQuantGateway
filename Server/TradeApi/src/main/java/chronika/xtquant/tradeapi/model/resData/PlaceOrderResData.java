package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.order.entity.OrderPlacingResult;
import io.swagger.v3.oas.annotations.media.Schema;

public class PlaceOrderResData {

    @Schema(description = "下单结果")
    private OrderPlacingResult result;

    //
    // Constructors
    //

    public PlaceOrderResData() {
    }

    public PlaceOrderResData(OrderPlacingResult result) {
        this.result = result;
    }

    //
    // Getters and Setters
    //

    public OrderPlacingResult getResult() {
        return result;
    }

    public void setResults(OrderPlacingResult result) {
        this.result = result;
    }

    //
    // Builder
    //

    public static PlaceOrderResDataBuilder builder() {
        return new PlaceOrderResDataBuilder();
    }

    public static class PlaceOrderResDataBuilder {
        private OrderPlacingResult result;

        public PlaceOrderResDataBuilder result(OrderPlacingResult result) {
            this.result = result;
            return this;
        }

        public PlaceOrderResData build() {
            return new PlaceOrderResData(result);
        }
    }

}
