package com.simplespider.test;
import com.simplespider.model.Site;
import com.simplespider.processor.SimplePageProcessor;
import com.simplespider.worker.Spider;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

public class ApplicationTest {
    private static Logger logger = Logger.getLogger(Test.class);
    @Test
    public void spiderTest(){
    }
}
