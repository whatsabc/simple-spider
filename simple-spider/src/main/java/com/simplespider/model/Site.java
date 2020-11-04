package com.simplespider.model;

import com.simplespider.useragent.UserAgentProvider;

import java.util.*;

/**
 * Site，要爬取的站点信息
 * @author Jianshu
 * @time 20201102
 */
public class Site {
    //主机
    private String domain;
    //userAgent池
    private UserAgentProvider userAgentProvider;
    //
    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();
    private Map<String, Map<String, String>> cookies = new HashMap<String, Map<String, String>>();
    private String charset;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public UserAgentProvider getUserAgentProvider() {
        return userAgentProvider;
    }

    public void setUserAgentProvider(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    public String getUserAgent() {
        return userAgentProvider.getUserAgent().getUserAgent();
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
