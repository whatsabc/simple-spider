package com.simplespider.model;

import com.simplespider.useragent.UserAgentProvider;

import java.util.*;

/**
 * Site，要爬取的站点信息
 * **以下set函数都使用了builder设计模式**
 * @author Jianshu
 * @since 20201102
 */
public class Site {
    //主机
    private String domain;
    //userAgent
    private String userAgent;
    //userAgent池
    private UserAgentProvider userAgentProvider;

    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();
    private Map<String, Map<String, String>> cookies = new HashMap<String, Map<String, String>>();
    private String charset;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        //每次提供一个随机的userAgent
        userAgent=userAgentProvider.getUserAgent();
        return userAgent;
    }

    public UserAgentProvider getUserAgentProvider() {
        return userAgentProvider;
    }

    /**
     * @param userAgentProvider
     * @return
     */
    public Site setUserAgentProvider(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
        return this;
    }

    public Map<String, String> getDefaultCookies() {
        return defaultCookies;
    }

    public void setDefaultCookies(Map<String, String> defaultCookies) {
        this.defaultCookies = defaultCookies;
    }

    public Map<String, Map<String, String>> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, Map<String, String>> cookies) {
        this.cookies = cookies;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
