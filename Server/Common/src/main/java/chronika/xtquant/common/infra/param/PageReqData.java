package chronika.xtquant.common.infra.param;

import chronika.xtquant.common.infra.misc.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PageReqData {

    @Schema(description = "每页数量, 最大5000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Min(1)
    @Max(5000)
    private Integer pageSize = Constants.defaultPageSize;

    @Schema(description = "页码, 从0开始", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Min(0)
    private Integer pageNum = 0;

    //
    // Constructors
    //

    public PageReqData() {
    }

    public PageReqData(Integer pageSize, Integer pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    //
    // Getters and Setters
    //

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

}
