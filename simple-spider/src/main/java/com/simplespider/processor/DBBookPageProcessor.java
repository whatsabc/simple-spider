package com.simplespider.processor;

import com.simplespider.downloader.HttpClientDownloader;
import com.simplespider.model.Page;
import com.simplespider.model.Site;
import com.simplespider.pipeline.ConsolePipeline;
import com.simplespider.pipeline.FilePipeline;
import com.simplespider.pipeline.database.DataBasePipeline;
import com.simplespider.proxy.Proxy;
import com.simplespider.proxy.SimpleProxyProvider;
import com.simplespider.useragent.SimpleUserAgentProvider;
import com.simplespider.worker.Spider;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBBookPageProcessor implements PageProcessor{

    protected Logger logger=Logger.getLogger(getClass());
    protected static List<String> userAgentList=new ArrayList<>();
    Site site=new Site().setUserAgentProvider(new SimpleUserAgentProvider(userAgentList));

    @Override
    public void process(Page page) {

        int sleepTime=5000;
        Document document= Jsoup.parse(page.getContent());

        String title=document.getElementsByTag("h1").text();
        page.putField("书名:",title);

        String elementsInfoItems=document.getElementById("info").text();
        String []infoItem=elementsInfoItems.split(" ");
        for(int i=0;i<infoItem.length-1;i=i+2){
            if(infoItem[i].equals("出版年:")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                try {
                    Date date = simpleDateFormat.parse(infoItem[i+1]);
                    page.putField(infoItem[i],date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if(infoItem[i].equals("定价:")){
                Pattern pattern= Pattern.compile("\\d+(\\.\\d+)?");
                Matcher matcher=pattern.matcher(infoItem[i+1]);
                Double price=new Double(0d);
                if(matcher.find()){
                    price=Double.valueOf(matcher.group());
                }
                page.putField("定价:",price);
                continue;
            }
            page.putField(infoItem[i],infoItem[i+1]);
        }

        page.putField("分类:","文学");

        String rating=document.getElementsByClass("ll rating_num ").get(0).text();
        page.putField("rating:",Double.valueOf(rating));


        Element elementRatingSum=document.getElementsByClass("rating_people").get(0);
        Integer ratingSum=Integer.valueOf(elementRatingSum.getElementsByTag("span").text());
        page.putField("ratingSum:",ratingSum);

        String mainPic=document.getElementById("mainpic").getElementsByClass("nbg").attr("href");
        page.putField("mainPic:",mainPic);

        page.putField("href:",page.getUrl());

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

        String url="https://book.douban.com/subject/4913064/";

        //加入起始urls
        List<String> urls=new ArrayList<>();
        urls.add(url);

        //设置爬虫
        Spider spider=new Spider(new DBBookPageProcessor());
        spider.addUrl(urls)
                .addPipeline(new DataBasePipeline())
                .setDownloader(new HttpClientDownloader())
                .setThreadNum(5)
                .run();
    }
}
