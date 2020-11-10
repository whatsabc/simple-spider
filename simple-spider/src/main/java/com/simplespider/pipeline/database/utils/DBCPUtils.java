package com.simplespider.pipeline.database.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

/**
 * @author Jianshu
 * @since 20201107
 * @TODO dbcp工具类（数据库连接池工具），用于管理多个数据库连接对象
 */
public class DBCPUtils {
    static DataSource ds = null;
    static {
        try {
            //得到配置文件
            Properties prop = new Properties();
            prop.load(DBCPUtils.class.getClassLoader().getResourceAsStream("datasource.properties"));
            //根据配置文件创建数据库连接池（数据源）
            ds = BasicDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("DBCP初始化异常");
        }
    }

    /**
     * 得到数据库连接对象
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有资源连接
     *
     * @param conn
     * @param ps
     * @param rs
     */
    public static void releaseAll(Connection conn, Statement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            conn = null;
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ps = null;
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rs = null;
        }
    }
}
