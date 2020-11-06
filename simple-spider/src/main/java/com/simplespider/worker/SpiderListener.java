package com.simplespider.worker;

import com.simplespider.model.Request;

/**
 * 两个钩子函数，实现此接口可以完成监视器
 * @author Jianshu
 * @since 20201105
 */
public interface SpiderListener {
    public void onSuccess(Request request);
    public void onError(Request request);
}
