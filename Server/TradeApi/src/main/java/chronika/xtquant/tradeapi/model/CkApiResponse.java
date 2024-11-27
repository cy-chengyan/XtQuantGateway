package chronika.xtquant.tradeapi.model;

import chronika.xtquant.common.infra.enums.CkError;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Response")
public class CkApiResponse<T> {

    @Schema(description = "Request ID")
    private String reqId;

    @Schema(description = "Error code")
    private int code;

    @Schema(description = "Error message")
    private String msg;

    @Schema(description = "Response data")
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //
    // Constructors
    //

    private CkApiResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private CkApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private CkApiResponse(CkError ckError, T data) {
        this.code = ckError.code;
        this.msg = ckError.toString();
        this.data = data;
    }

    private CkApiResponse(CkError ckError) {
        this.code = ckError.code;
        this.msg = ckError.toString();
    }

    //
    // Static methods
    //

    public static CkApiResponse<?> ok() {
        return new CkApiResponse<>(CkError.GK_OK);
    }

    public static <S> CkApiResponse<S> ok(S data) {
        return new CkApiResponse<>(CkError.GK_OK, data);
    }

    public static <S> CkApiResponse<S> error(CkError ckError) {
        return new CkApiResponse<>(ckError);
    }

    public static <S> CkApiResponse<S> error(CkError ckError, S data) {
        return new CkApiResponse<>(ckError, data);
    }

    public static <S> CkApiResponse<S> error(int code, String msg) {
        return new CkApiResponse<>(code, msg);
    }

    public static <S> CkApiResponse<S> error(int code, String msg, S data) {
        return new CkApiResponse<>(code, msg, data);
    }

}
