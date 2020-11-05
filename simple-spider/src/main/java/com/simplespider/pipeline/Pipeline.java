package com.simplespider.pipeline;

import com.simplespider.model.ResultItems;

public interface Pipeline {
    public void process(ResultItems resultItems);
}
