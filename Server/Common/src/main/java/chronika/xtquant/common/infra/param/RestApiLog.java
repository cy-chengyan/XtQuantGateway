package chronika.xtquant.common.infra.param;

import java.util.Map;


public class RestApiLog {

    private String endpoint;
    private String method;
    private Map<String, String> reqHeader;
    private Map<String, String[]> reqParams;

    private Long reqAt;
    private int reqSize;
    private String reqContentType;
    private Map<String, Object> reqBody;

    private Long resAt;
    private int resSize;
    private int resStatus;
    private Map<String, String> resHeader;
    private String resContentType;
    private Map<String, Object> resBody;

    private Long spend;
    private String tz;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(Map<String, String> reqHeader) {
        this.reqHeader = reqHeader;
    }

    public Map<String, String[]> getReqParams() {
        return reqParams;
    }

    public void setReqParams(Map<String, String[]> reqParams) {
        this.reqParams = reqParams;
    }

    public Long getReqAt() {
        return reqAt;
    }

    public void setReqAt(Long reqAt) {
        this.reqAt = reqAt;
    }

    public int getReqSize() {
        return reqSize;
    }

    public void setReqSize(int reqSize) {
        this.reqSize = reqSize;
    }

    public String getReqContentType() {
        return reqContentType;
    }

    public void setReqContentType(String reqContentType) {
        this.reqContentType = reqContentType;
    }

    public Map<String, Object> getReqBody() {
        return reqBody;
    }

    public void setReqBody(Map<String, Object> reqBody) {
        this.reqBody = reqBody;
    }

    public Long getResAt() {
        return resAt;
    }

    public void setResAt(Long resAt) {
        this.resAt = resAt;
    }

    public int getResSize() {
        return resSize;
    }

    public void setResSize(int resSize) {
        this.resSize = resSize;
    }

    public int getResStatus() {
        return resStatus;
    }

    public void setResStatus(int resStatus) {
        this.resStatus = resStatus;
    }

    public Map<String, String> getResHeader() {
        return resHeader;
    }

    public void setResHeader(Map<String, String> resHeader) {
        this.resHeader = resHeader;
    }

    public String getResContentType() {
        return resContentType;
    }

    public void setResContentType(String resContentType) {
        this.resContentType = resContentType;
    }

    public Map<String, Object> getResBody() {
        return resBody;
    }

    public void setResBody(Map<String, Object> resBody) {
        this.resBody = resBody;
    }

    public Long getSpend() {
        return spend;
    }

    public void setSpend(Long spend) {
        this.spend = spend;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

}
