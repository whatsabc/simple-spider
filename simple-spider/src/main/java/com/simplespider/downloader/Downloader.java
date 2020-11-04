package com.simplespider.downloader;

import com.simplespider.model.Page;
import com.simplespider.model.Request;
import com.simplespider.model.Site;
import org.apache.http.impl.client.CloseableHttpClient;

public interface Downloader {
    public Page download(Request request,Site site);
}
