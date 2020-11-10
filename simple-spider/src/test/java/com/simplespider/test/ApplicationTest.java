package com.simplespider.test;
import com.simplespider.pipeline.database.utils.DBCPUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationTest {
    private static Logger logger = Logger.getLogger(Test.class);

    private String hello;
    @Test
    public void spiderTest(){
        String string="https://img3.doubanio.com/view/subject/l/public/s29053580.jpg";
        System.out.println(string.length());
    }
}
