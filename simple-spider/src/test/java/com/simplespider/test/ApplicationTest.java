package com.simplespider.test;
import org.apache.log4j.Logger;
import org.junit.Test;

public class ApplicationTest {
    private static Logger logger = Logger.getLogger(Test.class);

    private String hello;
    @Test
    public void spiderTest(){
        System.out.println(this.hashCode());
        new Builder().test();

    }

    class Builder{
        public void test(){
            System.out.println(this.hashCode());
        }
    }
}
