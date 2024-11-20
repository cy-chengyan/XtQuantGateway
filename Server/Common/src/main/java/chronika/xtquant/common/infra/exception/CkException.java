package chronika.xtquant.common.infra.exception;

import chronika.xtquant.common.infra.enums.CkError;

import java.io.Serial;

/**
 * Ginkgo自定义异常
 */
public class CkException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;
    private final String msg;
    private Object data;

    public CkException(CkError err) {
        this.code = err.code;
        this.msg = err.toString();
    }

    public CkException(CkError err, Object data) {
        this.code = err.code;
        this.msg = err.msg;
        this.data = data;
    }

    public CkException(CkError err, String msg) {
        this.code = err.code;
        this.msg = msg;
    }

    public CkException(int code) {
        this.code = code;
        this.msg = CkError.getGkError(code).toString();
    }

    public CkException(Throwable e) {
        super(e);
        this.code = CkError.INTERNAL_SERVER_ERROR.code;
        this.msg = CkError.INTERNAL_SERVER_ERROR.msg;
    }

    public CkException(String msg) {
        super(msg);
        this.code = CkError.INTERNAL_SERVER_ERROR.code;
        this.msg = msg;
    }

    public CkException(String msg, Throwable e) {
        super(msg, e);
        this.code = CkError.INTERNAL_SERVER_ERROR.code;
        this.msg = msg;
    }

    public CkError getGkErr() {
        return CkError.getGkError(this.code);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}

