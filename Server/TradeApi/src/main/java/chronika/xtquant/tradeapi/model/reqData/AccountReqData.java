package chronika.xtquant.tradeapi.model.reqData;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AccountReqData {

    @Schema(description = "账号id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String accountId;

    //
    // Getters and Setters
    //

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

}
