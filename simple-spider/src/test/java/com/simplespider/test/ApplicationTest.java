package com.simplespider.test;
import com.simplespider.pipeline.database.utils.DBCPUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationTest {
    private static Logger logger = Logger.getLogger(Test.class);

    private String hello;
    @Test
    public void spiderTest(){
        String infoItems="作者: 余华 出版社: 作家出版社 出版年: 2012-8-1 页数: 191 定价: 20.00元 装帧: 平装 丛书: 余华作品（2012版） ISBN: 9787506365437";
        String []infoItem=infoItems.split(" ");

        String temp=new String();
        String key=new String();
        LinkedHashMap<String,String> linkedHashMap=new LinkedHashMap<>();
        for(int i=0;i<infoItem.length;i++){
            Pattern pattern= Pattern.compile("\\:");
            Matcher matcher=pattern.matcher(infoItem[i]);
            if(matcher.find()){
                linkedHashMap.put(key,temp);
                temp="";
                key=infoItem[i];
            }
            else if(i==infoItem.length-1){
                temp+=infoItem[i];
                linkedHashMap.put(key,temp);
            }
            else{
                temp+=infoItem[i];
            }
        }
        System.out.println();
    }
}
