package com.simplespider.scheduler;

import com.simplespider.model.Request;
import com.simplespider.scheduler.strategy.DuplicateRemover;
import com.simplespider.scheduler.strategy.HashSetDuplicateRemover;
import com.simplespider.utils.HttpConstant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 可去重调度器
 * @author Jianshu
 * @since 20201106
 */
public class DuplicateRemoveScheduler implements Scheduler{

    private DuplicateRemover duplicatedRemover = new HashSetDuplicateRemover();
    private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    @Override
    public void push(Request request) {
        //如果不在去重队列里面
        if(!duplicatedRemover.isDuplicate(request)||noNeedToRemoveDuplicate(request)){
            queue.add(request);
        }
    }

    @Override
    public Request poll() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * 如果是POST请求，一律视为不同
     * @param request
     * @return
     */
    protected boolean noNeedToRemoveDuplicate(Request request) {
        return HttpConstant.Method.POST.equalsIgnoreCase(request.getMethod());
    }
}
