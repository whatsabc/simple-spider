package com.simplespider.proxy;

import com.simplespider.model.Page;

import java.util.List;
import java.util.Random;

public class SimpleProxyProvider implements ProxyProvider{

    private final List<Proxy> proxyList;

    public SimpleProxyProvider(List<Proxy> proxyList) {
        this.proxyList = proxyList;
    }

    @Override
    public void returnProxy(Proxy proxy, Page page) {

    }

    @Override
    public Proxy getProxy() {
        return proxyList.get(randomProxy());
    }

    private int randomProxy() {
        Random random = new Random();
        int size = proxyList.size();
        return random.nextInt(size);
    }
}
