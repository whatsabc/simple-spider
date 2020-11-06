package com.simplespider.scheduler.strategy;

import com.simplespider.model.Request;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 使用Set的去重策略实现类
 * @author Jianshu
 * @since 20201106
 */
public class HashSetDuplicateRemover implements DuplicateRemover{

    private Set<String> urls=new CopyOnWriteArraySet<>();

    @Override
    public boolean isDuplicate(Request request) {
        return !urls.add(request.getUrl());
    }
}
