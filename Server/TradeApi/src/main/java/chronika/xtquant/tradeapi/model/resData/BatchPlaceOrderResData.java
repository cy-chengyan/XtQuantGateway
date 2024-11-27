package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.order.entity.OrderPlacingResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class BatchPlaceOrderResData {

    @Schema(description = "下单结果列表")
    private List<OrderPlacingResult> results;

    //
    // Constructors
    //

    public BatchPlaceOrderResData() {
    }

    public BatchPlaceOrderResData(List<OrderPlacingResult> results) {
        this.results = results;
    }

    //
    // Getters and Setters
    //

    public List<OrderPlacingResult> getResults() {
        return results;
    }

    public void setResults(List<OrderPlacingResult> results) {
        this.results = results;
    }

    //
    // Builder
    //

    public static PlaceOrderResDataBuilder builder() {
        return new PlaceOrderResDataBuilder();
    }

    public static class PlaceOrderResDataBuilder {
        private List<OrderPlacingResult> results;

        public PlaceOrderResDataBuilder results(List<OrderPlacingResult> results) {
            this.results = results;
            return this;
        }

        public BatchPlaceOrderResData build() {
            return new BatchPlaceOrderResData(results);
        }
    }

}
