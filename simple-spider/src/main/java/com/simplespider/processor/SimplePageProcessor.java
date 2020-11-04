package com.simplespider.processor;

import com.simplespider.model.Page;
import com.simplespider.model.Site;

/**
 * @author Jianshu
 * @time 20201102
 * 页面处理规则
 */
public class SimplePageProcessor implements PageProcessor{

    private Site site;

    @Override
    public void process(Page page) {

        System.out.println(page.getContent());
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
