package com.simplespider.scheduler.strategy;

import com.simplespider.model.Request;

/**
 * 去重策略接口
 * @author Jianshu
 * @since 20201106
 */
public interface DuplicateRemover {
    /**
     * @param request
     * @return
     */
    public boolean isDuplicate(Request request);
}
