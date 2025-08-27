package chronika.xtquant.common.infra.param;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SortParam {

    @Schema(description = "Sort fields", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> fields;

    @Schema(description = "Sort orders, \"ascend\", \"descend\"", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> orders;

    //
    // Constructor
    //

    public SortParam() {
    }

    public SortParam(List<String> fields, List<String> orders) {
        this.fields = fields;
        this.orders = orders;
    }

    //
    // Getters and Setters
    //

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

}
