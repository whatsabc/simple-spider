package com.simplespider.downloader;

import com.simplespider.model.Site;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * HttpClient生成类
 * @author Jianshu
 * @since 20201102
 */
public class HttpClientGenerator {

    public CloseableHttpClient generateClient(Site site) {
        HttpClientBuilder httpClientBuilder=HttpClients.custom();
        httpClientBuilder.setUserAgent(site.getUserAgent());
        return httpClientBuilder.build();
    }
}
