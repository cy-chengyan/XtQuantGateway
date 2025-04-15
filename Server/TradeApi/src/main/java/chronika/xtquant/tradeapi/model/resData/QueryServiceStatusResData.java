package chronika.xtquant.tradeapi.model.resData;

import chronika.xtquant.common.gateway.entity.ServiceStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class QueryServiceStatusResData {

    @Schema(description = "服务状态")
    private ServiceStatus status;

    //
    // Constructors
    //

    public QueryServiceStatusResData() {
    }

    public QueryServiceStatusResData(ServiceStatus status) {
        this.status = status;
    }

    //
    // Getters and Setters
    //

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    //
    // Builder
    //

    public static QueryServiceStatusResDataBuilder builder() {
        return new QueryServiceStatusResDataBuilder();
    }

    public static class QueryServiceStatusResDataBuilder {
        private ServiceStatus status;

        public QueryServiceStatusResDataBuilder status(ServiceStatus status) {
            this.status = status;
            return this;
        }

        public QueryServiceStatusResData build() {
            return new QueryServiceStatusResData(status);
        }
    }

}
