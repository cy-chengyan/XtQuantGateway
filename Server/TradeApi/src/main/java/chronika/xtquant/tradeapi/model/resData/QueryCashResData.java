package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.asset.entity.Asset;
import chronika.xtquant.common.infra.param.PageResData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class QueryCashResData extends PageResData {

    @Schema(description = "cash list")
    private List<Asset> cashes;

    //
    // Constructors
    //

    public QueryCashResData() {
    }

    public QueryCashResData(List<Asset> cashes, Integer totalPages, Long totalElements, Integer pageSize, Integer pageNum) {
        super(totalPages, totalElements, pageSize, pageNum);
        this.cashes = cashes;
    }

    //
    // Getters and Setters
    //

    public List<Asset> getCashes() {
        return cashes;
    }

    public void setCashes(List<Asset> cashes) {
        this.cashes = cashes;
    }

    //
    // Builder
    //

    public static QueryCashResDataBuilder builder() {
        return new QueryCashResDataBuilder();
    }

    public static class QueryCashResDataBuilder {
        private List<Asset> cashes;
        private Integer totalPages;
        private Long totalElements;
        private Integer pageSize;
        private Integer pageNum;

        public QueryCashResDataBuilder cashes(List<Asset> cashes) {
            this.cashes = cashes;
            return this;
        }

        public QueryCashResDataBuilder totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public QueryCashResDataBuilder totalElements(Long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public QueryCashResDataBuilder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public QueryCashResDataBuilder pageNum(Integer pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public QueryCashResData build() {
            return new QueryCashResData(cashes, totalPages, totalElements, pageSize, pageNum);
        }
    }

}
