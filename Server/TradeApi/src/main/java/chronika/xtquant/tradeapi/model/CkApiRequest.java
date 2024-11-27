package chronika.xtquant.tradeapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Request")
public class CkApiRequest<T> {

    @Schema(description = "Request ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String reqId;

    @Schema(description = "业务数据")
    @JsonProperty("data")
    @Valid
    private T data;

    //
    // Getters and Setters
    //

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
