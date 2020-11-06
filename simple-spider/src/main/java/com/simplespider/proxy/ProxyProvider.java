package com.simplespider.proxy;

import com.simplespider.model.Page;

public interface ProxyProvider {
    void returnProxy(Proxy proxy, Page page);
    Proxy getProxy();
}
