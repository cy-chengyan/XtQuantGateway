package chronika.xtquant.common.infra.enums;

/**
 * 错误编码，由6位数字组成，前3位为模块编码，后3位为业务编码
 * <p>
 * 如：100001（100代表系统模块，001代表业务代码）
 * </p>
 */
public enum CkError {

    GK_OK(0, "ok"),

    UNKNOWN_ERROR(100001, "Unknown error"),
    INTERNAL_SERVER_ERROR(100002, "Internal server error"),
    RECORD_NOT_FOUND(100003, "Record not found"),
    MISSING_PARAM(100004, "Missing parameter"),
    INVALID_PARAM(100005, "Invalid parameter"),
    FUNC_NOT_SUPPORTED(100006, "Function not supported"),
    RECORD_ALREADY_EXISTS(100007, "Record already exists"),
    ;

    public final int code;
    public final String msg;

    CkError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }

    public static CkError getGkError(int code) {
        for (CkError err : CkError.values()) {
            if (err.code == code) {
                return err;
            }
        }
        return UNKNOWN_ERROR;
    }

    public static String toString(CkError err) {
        return err.code + ", " + err.msg;
    }

}
