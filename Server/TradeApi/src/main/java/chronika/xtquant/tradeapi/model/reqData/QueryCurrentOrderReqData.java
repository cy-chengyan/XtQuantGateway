package chronika.xtquant.tradeapi.model.reqData;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class QueryCurrentOrderReqData {

    @Schema(description = "账号id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String accountId;

    @Schema(description = "定单状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<@NotNull Integer> status;

    //
    // Getters and Setters
    //

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

}
