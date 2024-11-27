package chronika.xtquant.tradeapi.model.reqData;

import chronika.xtquant.common.infra.param.PageReqData;
import io.swagger.v3.oas.annotations.media.Schema;

public class QueryCashReqData extends PageReqData {

    @Schema(description = "账号id")
    private String accountId;

    @Schema(description = "日期, YYYYMMDD")
    private Integer date;

    //
    // Getters and Setters
    //

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

}
