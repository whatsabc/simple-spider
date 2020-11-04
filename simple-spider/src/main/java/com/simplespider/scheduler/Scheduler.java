package com.simplespider.scheduler;

import com.simplespider.model.Request;

public interface Scheduler {

    public void push(Request request);
    public Request poll();
    public boolean isEmpty();

}
