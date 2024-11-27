package chronika.xtquant.common.infra.param;

import io.swagger.v3.oas.annotations.media.Schema;

public class PageResData {

    @Schema(description = "Total pages")
    private Integer totalPages;

    @Schema(description = "Total elements")
    private Long totalElements;

    @Schema(description = "Page size")
    private Integer pageSize;

    @Schema(description = "Current page number, start from 0")
    private Integer pageNum;

    //
    // Constructors
    //

    public PageResData() {
    }

    public PageResData(Integer totalPages, Long totalElements, Integer pageSize, Integer pageNum) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    //
    // Getters and Setters
    //

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

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
