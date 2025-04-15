package chronika.xtquant.common.gateway.entity;

import chronika.xtquant.common.infra.util.JsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "t_service_status")
public class ServiceStatus {

    public static final int SERVICE_STATUS_OK = 1;
    public static final int SERVICE_STATUS_ERROR = 2;

    @Schema(description = "服务id")
    @Id
    private String id;

    @Schema(description = "状态, 1:正常, 2:错误")
    private int status;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "更新时间戳(秒)")
    @Column(name = "`updated_at`", insertable = false, updatable = false)
    private Timestamp updatedAt;

    //
    // Constructor
    //

    public ServiceStatus() {
    }

    public ServiceStatus(String id, int status, String errorMsg) {
        this.id = id;
        this.status = status;
        this.errorMsg = errorMsg;
    }

    //
    // Getters and Setters
    //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    //
    // Other methods
    //

    @Override
    public String toString() {
        return "ServiceStatus:" + JsonUtil.toJsonString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceStatus other)) {
            return false;
        }
        return this.checkSum().equals(other.checkSum());
    }

    private String checkSum() {
        return id + ";"
            + status + ";"
            + errorMsg;
    }

    public static ServiceStatus createByFeedLine(String id, String[] lineFields) {
        if (lineFields == null || lineFields.length < 10) {
            return null;
        }

        String msg = lineFields[8];
        return msg.equals("登录成功")
            ? new ServiceStatus(id, SERVICE_STATUS_OK, "OK")
            : new ServiceStatus(id, SERVICE_STATUS_ERROR, msg);
    }

}
