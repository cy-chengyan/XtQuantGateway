package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.asset.entity.Asset;
import io.swagger.v3.oas.annotations.media.Schema;

public class QueryCurrentCashResData {

    @Schema(description = "Cash")
    private Asset cash;

    //
    // Constructors
    //

    public QueryCurrentCashResData() {
    }

    public QueryCurrentCashResData(Asset cash) {
        this.cash = cash;
    }

    //
    // Getters and Setters
    //

    public Asset getCash() {
        return cash;
    }

    public void setCash(Asset cash) {
        this.cash = cash;
    }

    //
    // Builder
    //

    public static QueryCurrentCashResDataBuilder builder() {
        return new QueryCurrentCashResDataBuilder();
    }

    public static class QueryCurrentCashResDataBuilder {
        private Asset cash;

        public QueryCurrentCashResDataBuilder cash(Asset cash) {
            this.cash = cash;
            return this;
        }

        public QueryCurrentCashResData build() {
            return new QueryCurrentCashResData(cash);
        }
    }

}
