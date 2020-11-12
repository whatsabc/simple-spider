package com.simplespider.pipeline;

import com.simplespider.model.ResultItems;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 控制台输出管道
 * @author Jianshu
 * @since 20201105
 */
public class ConsolePipeline implements Pipeline{

    protected Logger logger=Logger.getLogger(ConsolePipeline.class);

    @Override
    public void process(ResultItems resultItems) {
        logger.info("[[ConsolePipeline]] is running...");
        for (Map.Entry<String,Object> entry:resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey()+entry.getValue());
        }
    }
}
