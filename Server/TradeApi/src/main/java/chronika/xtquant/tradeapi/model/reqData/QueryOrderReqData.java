package chronika.xtquant.tradeapi.model.reqData;

import chronika.xtquant.common.infra.param.PageReqData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QueryOrderReqData extends PageReqData {

    @Schema(description = "账号id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String accountId;

    @Schema(description = "定单sid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sid;

    @Schema(description = "日期, YYYYMMDD", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Min(20210101)
    private Integer date;

    @Schema(description = "定单状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<@NotNull Integer> status;

    @Schema(description = "股票代码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String stockCode;

    //
    // Getters and Setters
    //

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<@NotNull Integer> getStatus() {
        return status;
    }

    public void setStatus(List<@NotNull Integer> status) {
        this.status = status;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

}
