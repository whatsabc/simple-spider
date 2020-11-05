package com.simplespider.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Page是对Downloader下载结果的封装。同时他也是PageProcessor待处理的核心对象
 */
public class Page {
    private String url;
    private Integer statusCode;
    private String content;
    private ResultItems resultItems=new ResultItems();

    //该页中还需要爬取的页面
    private List<Request> targetRequests = new ArrayList<Request>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Request> getTargetRequests() {
        return targetRequests;
    }

    public void setTargetRequests(List<Request> targetRequests) {
        this.targetRequests = targetRequests;
    }

    public void putField(String key,Object field){
        resultItems.put(key,field);
    }

    public ResultItems getResultItems() {
        return resultItems;
    }

    public void setResultItems(ResultItems resultItems) {
        this.resultItems = resultItems;
    }
}
