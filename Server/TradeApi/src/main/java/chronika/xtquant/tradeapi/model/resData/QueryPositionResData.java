package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.infra.param.PageResData;
import chronika.xtquant.common.position.entity.Position;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class QueryPositionResData extends PageResData {

    @Schema(description = "Position list")
    private List<Position> positions;

    //
    // Constructors
    //

    public QueryPositionResData() {
    }

    public QueryPositionResData(List<Position> positions, Integer totalPages, Long totalElements, Integer pageSize, Integer pageNum) {
        super(totalPages, totalElements, pageSize, pageNum);
        this.positions = positions;
    }

    //
    // Getters and Setters
    //

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    //
    // Builder
    //

    public static QueryPositionResDataBuilder builder() {
        return new QueryPositionResDataBuilder();
    }

    public static class QueryPositionResDataBuilder {
        private List<Position> positions;
        private Integer totalPages;
        private Long totalElements;
        private Integer pageSize;
        private Integer pageNum;

        public QueryPositionResDataBuilder positions(List<Position> positions) {
            this.positions = positions;
            return this;
        }

        public QueryPositionResDataBuilder totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public QueryPositionResDataBuilder totalElements(Long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public QueryPositionResDataBuilder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public QueryPositionResDataBuilder pageNum(Integer pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public QueryPositionResData build() {
            return new QueryPositionResData(positions, totalPages, totalElements, pageSize, pageNum);
        }
    }

}
