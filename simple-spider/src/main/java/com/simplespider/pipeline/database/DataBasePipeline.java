package com.simplespider.pipeline.database;

import com.simplespider.model.ResultItems;
import com.simplespider.pipeline.ConsolePipeline;
import com.simplespider.pipeline.Pipeline;
import com.simplespider.pipeline.database.model.Book;
import com.simplespider.pipeline.database.utils.DBCPUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Jianshu
 * @since 20201109
 */
public class DataBasePipeline implements Pipeline {

    protected Logger logger=Logger.getLogger(DataBasePipeline.class);
    @Override
    public void process(ResultItems resultItems) {
        logger.info("[[DataBasePipeline]] is running...");
        Connection connection= DBCPUtils.getConnection();
        String insertSQL="INSERT INTO mall_book(book_isbn,book_name,book_author,book_translator," +
                "book_press,book_date,book_price,book_stock,book_status,book_category,book_rating," +
                "book_rating_sum,book_pic,book_href)"+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,(String)resultItems.get("ISBN:"));
            preparedStatement.setString(2,(String)resultItems.get("书名:"));
            preparedStatement.setString(3,(String)resultItems.get("作者:"));
            preparedStatement.setString(4,(String)resultItems.get("译者:"));
            preparedStatement.setString(5,(String)resultItems.get("出版社:"));

            java.util.Date date=resultItems.get("出版年:");
            java.sql.Date dateSQL=new java.sql.Date(date.getTime());
            preparedStatement.setDate(6, dateSQL);

            preparedStatement.setDouble(7,(Double)resultItems.get("定价:"));
            preparedStatement.setInt(8,100);//默认库存
            preparedStatement.setInt(9,1);//在售状态
            preparedStatement.setString(10,(String)resultItems.get("分类:"));
            preparedStatement.setDouble(11,(Double)resultItems.get("rating:"));
            preparedStatement.setInt(12,(Integer) resultItems.get("ratingSum:"));
            preparedStatement.setString(13,(String)resultItems.get("mainPic:"));
            preparedStatement.setString(14,(String)resultItems.get("href:"));
            preparedStatement.execute();
            logger.info("[[DataBasePipeline]] Data insert success");
            connection.close();
        } catch (SQLException throwables) {
            logger.error("[[DataBasePipeline]] Data insert error");
            throwables.printStackTrace();
        }
    }
}
