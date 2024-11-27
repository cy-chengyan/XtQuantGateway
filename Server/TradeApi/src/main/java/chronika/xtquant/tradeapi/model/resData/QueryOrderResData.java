package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.infra.param.PageResData;
import chronika.xtquant.common.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class QueryOrderResData extends PageResData {

    @Schema(description = "Order list")
    private List<Order> orders;

    //
    // Constructors
    //

    public QueryOrderResData() {
    }

    public QueryOrderResData(List<Order> orders, Integer totalPages, Long totalElements, Integer pageSize, Integer pageNum) {
        super(totalPages, totalElements, pageSize, pageNum);
        this.orders = orders;
    }

    //
    // Getters and Setters
    //

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    //
    // Builder
    //

    public static QueryOrderResDataBuilder builder() {
        return new QueryOrderResDataBuilder();
    }

    public static class QueryOrderResDataBuilder {
        private List<Order> orders;
        private Integer totalPages;
        private Long totalElements;
        private Integer pageSize;
        private Integer pageNum;

        public QueryOrderResDataBuilder orders(List<Order> orders) {
            this.orders = orders;
            return this;
        }

        public QueryOrderResDataBuilder totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public QueryOrderResDataBuilder totalElements(Long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public QueryOrderResDataBuilder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public QueryOrderResDataBuilder pageNum(Integer pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public QueryOrderResData build() {
            return new QueryOrderResData(orders, totalPages, totalElements, pageSize, pageNum);
        }
    }

}
