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
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBBookPageProcessor implements PageProcessor{

    protected Logger logger=Logger.getLogger(getClass());
    protected static List<String> userAgentList=new ArrayList<>();
    Site site=new Site().setUserAgentProvider(new SimpleUserAgentProvider(userAgentList));

    @Override
    public void process(Page page) {

        int sleepTime=8000;
        Document document= Jsoup.parse(page.getContent());

        String title=document.getElementsByTag("h1").text();
        page.putField("书名:",title);

        Element elementsInfo=document.getElementById("info");
        String infoItems=elementsInfo.text();
        String []infoItem=infoItems.split(" ");

        String temp=new String();
        String key=new String();
        for(int i=0;i<infoItem.length;i++){
            Pattern pattern= Pattern.compile("\\:");
            Matcher matcher=pattern.matcher(infoItem[i]);
            if(matcher.find()){
                page.putField(key,temp);
                temp="";
                key=infoItem[i];
            }
            else if(i==infoItem.length-1){
                temp+=infoItem[i];
                page.putField(key,temp);
            }
            else{
                temp+=infoItem[i];
            }
        }

        String dateString=page.getResultItems().get("出版年:");
        Calendar calendar=Calendar.getInstance();
        String []dates=dateString.split("-");
        int []integer=new int[3];
        for(int i=0;i<dates.length;i++){
            integer[i]=Integer.valueOf(dates[i]);
        }
        calendar.set(integer[0],integer[1],integer[2]);
        Date date = calendar.getTime();
        page.putField("出版年:", date);

        String priceString=page.getResultItems().get("定价:");
        Pattern pattern= Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcherPrice=pattern.matcher(priceString);
        Double priceDouble=new Double(0d);
        if(matcherPrice.find()){
            priceDouble=Double.valueOf(matcherPrice.group());
        }
        page.putField("定价:",priceDouble);

        page.putField("分类:","小说");

        String rating=document.getElementsByClass("ll rating_num ").get(0).text();
        page.putField("rating:",Double.valueOf(rating));


        Element elementRatingSum=document.getElementsByClass("rating_people").get(0);
        Integer ratingSum=Integer.valueOf(elementRatingSum.getElementsByTag("span").text());
        page.putField("ratingSum:",ratingSum);

        String mainPic=document.getElementById("mainpic").getElementsByClass("nbg").attr("href");
        page.putField("mainPic:",mainPic);

        page.putField("href:",page.getUrl());

        logger.info("[[StatusCode]]"+page.getStatusCode()+"[[Url]]"+page.getUrl());

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

        List<String> stringList=new ArrayList<>();
        //stringList.add("https://book.douban.com/subject/34949694/");//TEST后删除

        String filename="小说";
        String filePath="D:\\spider-download\\"+filename+".txt";
        try {
            BufferedReader bufferedReader= new BufferedReader(new FileReader(filePath));
            String temp;
            while((temp=bufferedReader.readLine())!=null){
                stringList.add(temp);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置爬虫
        Spider spider=new Spider(new DBBookPageProcessor());
        spider.addUrl(stringList)
                .addPipeline(new DataBasePipeline()).addPipeline(new ConsolePipeline())
                .setDownloader(new HttpClientDownloader())
                .setThreadNum(1)
                .run();
    }
}
