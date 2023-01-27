package com.cooperation.record.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接池工具类
 * @author cyl
 * @date 2021/10/07
 */
public class JdbcUtil {
    /**
     * 创建数据库连接池对象
     */
    private static DataSource ds;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /**
     * 使用静态代码块加载配置文件并初始化数据库连接池对象
     */
    static {
        try {
        // 将配置文件加载到内存里面
            Properties pro = new Properties();
            InputStream is = JdbcUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            pro.load(is);
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接池对象
     */
    public static DataSource getDataSource(){
        return ds;
    }

    /**
     * 获取连接对象
     * @return 返回连接对象
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        //如果是第一次，就创建一个连接
        if (conn == null) {
            conn = ds.getConnection();
            //添加到本地的线程变量
            threadLocal.set(conn);
        }
        return conn;
    }

    /**
     * 关闭连接
     * @param rs
     * @param pre
     * @param conn
     * @throws Exception
     */
    public static void close(ResultSet rs, PreparedStatement pre, Connection conn) throws Exception{
        if(rs != null){
            rs.close();
        }
        if(pre != null){
            pre.close();
        }
        if(conn != null){
            conn.close();
        }
    }

    /**
     * 开启事务
     */
    public static Connection beginTransaction() throws SQLException {
        Connection connection = getConnection();
        if(connection != null){
            connection.setAutoCommit(false);
        }
        return connection;
    }

    /**
     * 关闭事务
     */
    public static void closeTransaction() throws SQLException {
        Connection connection = getConnection();
        if(connection != null){
            connection.close();
        }
        threadLocal.remove();
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() throws SQLException {
        Connection connection = getConnection();
        if(connection != null){
            connection.commit();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() throws SQLException {
        Connection connection = getConnection();
        if(connection != null){
            connection.rollback();
        }
    }
}
