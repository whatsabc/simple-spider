package com.simplespider.downloader;

import com.simplespider.model.Page;
import com.simplespider.model.Request;
import com.simplespider.model.Site;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * 下载器抽象类
 * @author Jianshu
 * @since 20201102
 */
public interface Downloader {
    public Page download(Request request,Site site);
}
