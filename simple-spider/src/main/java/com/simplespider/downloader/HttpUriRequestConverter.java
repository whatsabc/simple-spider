package com.simplespider.downloader;

import com.simplespider.model.Request;
import com.simplespider.model.Site;
import com.simplespider.proxy.Proxy;
import com.simplespider.utils.HttpConstant;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

/**
 * HttpUriRequest生成类
 */
public class HttpUriRequestConverter {

    public HttpUriRequest covertHttpUriRequest(Request request, Site site, Proxy proxy){
        RequestBuilder requestBuilder=selectRequestMethod(request).setUri(request.getUrl());
        RequestConfig.Builder requestConfigBuilder=RequestConfig.custom();
        if(proxy!=null) {
            requestConfigBuilder.setProxy(new HttpHost(proxy.getHost(),proxy.getPort()));
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        HttpUriRequest httpUriRequest = requestBuilder.build();
        return httpUriRequest;
    }

    private RequestBuilder selectRequestMethod(Request request) {
        String method=request.getMethod();
        if (method==null||method.equalsIgnoreCase(HttpConstant.Method.GET)) {
            //default get
            return RequestBuilder.get();
        }
        return null;
    }
}
