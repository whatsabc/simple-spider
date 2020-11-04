package com.simplespider;

import com.simplespider.downloader.Downloader;
import com.simplespider.downloader.HttpClientDownloader;
import com.simplespider.model.Site;
import com.simplespider.processor.SimplePageProcessor;
import com.simplespider.proxy.Proxy;
import com.simplespider.proxy.SimpleProxyProvider;
import com.simplespider.useragent.SimpleUserAgentProvider;
import com.simplespider.useragent.UserAgent;
import com.simplespider.worker.Spider;

import javax.print.SimpleDoc;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jianshu
 * @time 20201102
 * @version 0.1
 */
public class SpiderStart {
    public static void main(String[] args){
        //UserAgent池
        List<UserAgent> userAgentList=new ArrayList<>();
        userAgentList.add(new UserAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36 Edg/86.0.622.56"));
        SimpleUserAgentProvider simpleUserAgentProvider=new SimpleUserAgentProvider(userAgentList);

        //代理池
        List<Proxy> proxyList=new ArrayList<>();
        SimpleProxyProvider  simpleProxyProvider=new SimpleProxyProvider(proxyList);

        //下载器
        Downloader proxyAndUserAgentPoolDownloader=new HttpClientDownloader();

        //新建站点，站点加入userAgent池
        Site site=new Site();
        site.setUserAgentProvider(simpleUserAgentProvider);

        //处理器
        SimplePageProcessor simplePageProcessor=new SimplePageProcessor();

        //设置站点信息
        simplePageProcessor.setSite(site);

        Spider spider=new Spider(simplePageProcessor);
        spider.addUrl("https://www.baidu.com").setDownloader(proxyAndUserAgentPoolDownloader).run();
    }
}
