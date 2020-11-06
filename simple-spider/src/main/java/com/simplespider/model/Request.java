package com.simplespider.model;

/**
 * Request是对url的一层封装。一个url对应一个Request对象，同时他也是PageProcessor通过Scheduler来管理Downloader的媒介类
 * @author Jianshu
 * @since 20201101
 */
public class Request {

    private String url;
    private String method;

    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
