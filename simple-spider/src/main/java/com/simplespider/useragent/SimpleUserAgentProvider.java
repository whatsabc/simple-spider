package com.simplespider.useragent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleUserAgentProvider implements UserAgentProvider{

    List<UserAgent> userAgentList;

    public SimpleUserAgentProvider(List<UserAgent> userAgentList) {
        this.userAgentList = userAgentList;
    }

    @Override
    public UserAgent getUserAgent() {
        Random random = new Random();
        int size = userAgentList.size();
        return userAgentList.get(random.nextInt(size));
    }
}
