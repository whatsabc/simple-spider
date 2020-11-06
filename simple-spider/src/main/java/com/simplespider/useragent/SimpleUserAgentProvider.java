package com.simplespider.useragent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleUserAgentProvider implements UserAgentProvider{

    List<String> userAgentList;
    public SimpleUserAgentProvider(List<String> userAgentList) {
        this.userAgentList = userAgentList;
    }

    @Override
    public String getUserAgent() {
        return userAgentList.get(randomUserAgent());
    }

    /**
     * 随机数生成器
     * @return
     */
    private int randomUserAgent() {
        Random random = new Random();
        int size = userAgentList.size();
        return random.nextInt(size);
    }
}
