package com.simplespider.downloader;

import com.simplespider.model.Page;
import com.simplespider.model.Request;
import com.simplespider.model.Site;
import com.simplespider.proxy.Proxy;
import com.simplespider.proxy.ProxyProvider;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * @author Jianshu
 * @time 20201101
 * 默认HttpClient下载器
 */
public class HttpClientDownloader implements Downloader{

    //代理提供器
    private ProxyProvider proxyProvider;
    //HttpClient创建
    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
    //HttpUriRequest创建
    private HttpUriRequestConverter httpUriRequestConverter=new HttpUriRequestConverter();

    /**
     * 设置代理
     * @param proxyProvider
     */
    public HttpClientDownloader setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
        return this;
    }

    /**
     * 进行request下载
     * @param request
     * @param site
     * @return
     */
    @Override
    public Page download(Request request,Site site){
        Page page=new Page();
        CloseableHttpClient closeableHttpClient=getHttpClient(site);
        CloseableHttpResponse closeableHttpResponse;
        //获取代理
        Proxy proxy=null;
        if(proxyProvider!=null){
            proxy=proxyProvider.getProxy()!=null?proxyProvider.getProxy():null;
        }
        HttpUriRequest httpUriRequest=httpUriRequestConverter.covertHttpUriRequest(request,site,proxy);
        try{
            HttpGet httpGet=new HttpGet(request.getUrl());
            closeableHttpResponse=closeableHttpClient.execute(httpUriRequest);
            String content=IOUtils.toString(closeableHttpResponse.getEntity().getContent(),"utf-8");
            page.setUrl(request.getUrl());
            page.setContent(content);
            page.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    /**
     * 得到HttpClient
     * @param site
     * @return
     */
    private CloseableHttpClient getHttpClient(Site site){
        CloseableHttpClient closeableHttpClient;
        closeableHttpClient=httpClientGenerator.generateClient(site);
        return closeableHttpClient;
    }

}
