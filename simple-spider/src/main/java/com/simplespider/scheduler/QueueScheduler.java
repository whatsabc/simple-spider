package com.simplespider.scheduler;

import com.simplespider.model.Request;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 最基本的基于内存的调度器，一个阻塞队列实现
 * @author Jianshu
 * @since 20201102
 */
public class QueueScheduler implements Scheduler{

    private BlockingDeque<Request> blockingDeque=new LinkedBlockingDeque<>();

    @Override
    public void push(Request request) {
        blockingDeque.add(request);
    }

    @Override
    public Request poll() {
        return blockingDeque.poll();
    }

    @Override
    public boolean isEmpty() {
        return blockingDeque.isEmpty();
    }
}
