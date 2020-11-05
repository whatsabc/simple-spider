package com.simplespider.processor;

import com.simplespider.downloader.HttpClientDownloader;
import com.simplespider.model.Page;
import com.simplespider.model.Site;
import com.simplespider.pipeline.ConsolePipeline;
import com.simplespider.pipeline.FilePipeline;
import com.simplespider.proxy.Proxy;
import com.simplespider.proxy.SimpleProxyProvider;
import com.simplespider.useragent.SimpleUserAgentProvider;
import com.simplespider.worker.Spider;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jianshu
 * @time 20201102
 * 页面处理规则，需要自己实现，实现了一个爬取豆瓣读书的范例
 */
public class DouBanPageProcessor implements PageProcessor{

    protected Logger logger=Logger.getLogger(DouBanPageProcessor.class);

    protected static List<String> userAgentList=new ArrayList<>();

    Site site=new Site().setUserAgentProvider(new SimpleUserAgentProvider(userAgentList));

    @Override
    public void process(Page page) {
        int sleepTime=5000;
        Document document= Jsoup.parse(page.getContent());
        Elements elements=document.getElementById("subject_list").getElementsByClass("nbg");
        for(Element element:elements){
            String href=element.attr("href");
            String key=String.valueOf(element.hashCode());
            page.putField(key,href);
            logger.info("key: "+key+" "+href+" process finished");
        }
        try {
            Thread.sleep(sleepTime);
            logger.info("sleeping "+sleepTime+" millis...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void setUserAgentList(List<String> userAgentList) {
        this.userAgentList = userAgentList;
    }

    public static void main(String[] args){
        //UserAgent池
        userAgentList.add(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36 Edg/86.0.622.56");
        userAgentList.add(
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        userAgentList.add(
                "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
        userAgentList.add(
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");

        //代理池（暂时没有用到）
        List<Proxy> proxyList=new ArrayList<>();
        proxyList.add(new Proxy("1.196.241.185",4285));
        proxyList.add(new Proxy("36.56.150.120",4256));
        proxyList.add(new Proxy("60.161.42.73",4220));
        proxyList.add(new Proxy("60.161.42.73",4285));
        proxyList.add(new Proxy("111.126.88.176",4283));
        proxyList.add(new Proxy("122.194.87.110",4231));
        proxyList.add(new Proxy("61.138.52.183",4237));
        proxyList.add(new Proxy("42.55.181.1",4285));
        proxyList.add(new Proxy("139.213.9.26",4224));

        SimpleProxyProvider simpleProxyProvider=new SimpleProxyProvider(proxyList);

        //新建爬虫对象，注入处理器
        Spider spider=new Spider(new DouBanPageProcessor());

        //加入起始urls
        List<String> urls=new ArrayList<>();
        urls.add("https://book.douban.com/tag/%E7%AE%97%E6%B3%95?start=0&type=T");
        for(int i=20;i<=980;i=i+20){
            urls.add("https://book.douban.com/tag/%E7%AE%97%E6%B3%95?start="+i+"&type=T");
        }

        spider.addUrl(urls)
                .addPipeline(new FilePipeline())
                .setDownloader(new HttpClientDownloader())
                .setThreadNum(5)
                .run();
    }
}
