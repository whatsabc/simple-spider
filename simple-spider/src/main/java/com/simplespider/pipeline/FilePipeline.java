package com.simplespider.pipeline;

import com.simplespider.model.ResultItems;
import com.simplespider.worker.Spider;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FilePipeline implements Pipeline{
    protected Logger logger=Logger.getLogger(FilePipeline.class);
    @Override
    public void process(ResultItems resultItems) {
        String fileName="算法.txt";
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName,true));
            logger.info("file "+fileName+" open success");
            for(Map.Entry<String,Object> entry:resultItems.getAll().entrySet()){
                bufferedWriter.write(entry.getValue().toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("file IOException");
        }
    }
}
