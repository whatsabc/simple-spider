package com.simplespider.pipeline;

import com.simplespider.model.ResultItems;
import com.simplespider.worker.Spider;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 文件传输管道
 * @author Jianshu
 * @since 20201105
 */
public class FilePipeline implements Pipeline{

    protected Logger logger=Logger.getLogger(FilePipeline.class);

    String filePath;

    public FilePipeline setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    @Override
    public void process(ResultItems resultItems) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath,true));
            logger.info("file "+filePath+" open success");
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
