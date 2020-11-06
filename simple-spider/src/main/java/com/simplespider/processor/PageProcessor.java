package com.simplespider.processor;

import com.simplespider.model.Page;
import com.simplespider.model.Site;

/**
 * @author Jianshu
 * @since 20201101
 */
public interface PageProcessor {
    public void process(Page page);
    public Site getSite();
}
