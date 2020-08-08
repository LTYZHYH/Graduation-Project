package com.example.travelapplication.service.valueobject;

public class BaseFilterCondition {
    public Integer pageNum = 0;
    public Integer pageSize = 10;
    public Integer totalPages = 0;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void resetData() {
        pageNum = 0;
        pageSize = 10;
        totalPages = 0;
    }
}
