package com.simplespider.pipeline;

import com.simplespider.model.ResultItems;

import java.util.Map;

public class ConsolePipeline implements Pipeline{
    @Override
    public void process(ResultItems resultItems) {
        for (Map.Entry<String,Object> entry:resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey()+": " + entry.getValue());
        }
    }
}
