package com.cooperation.record.dao.Mapper;

import com.cooperation.record.utils.JdbcUtil;
import com.cooperation.record.utils.LinkSqlUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 所有dao包中的类都要实现的抽象类
 * 使用模板模式，将共有的方法抽象出来，使具体操作延迟到子类中进行
 * 使用泛型，将父类转成自己的样子
 * 因为是想做尽可能通用一点，所以除了使用Javabean，还要使用一下Map集合
 * @author cyl
 * @date 2021/10/07
 */
public abstract class SimpleBaseMapper<T> implements BaseMapper<T> {
    /**
     * 获取表名
     * @return 返回一个pojo类的名字，也就是对应表的名字
     */
    public abstract String getTableName();

    /**
     * 在子类中根据具体需求来拼接查询条件（主要还是select的使用）
     * 在更新和select中会遇到
     * @return 返回一个拼接而成查询语句的字符串
     */
    public abstract String getQueryCondition(T object, List<Object> list);

    /**
     * 子类中使用select中where内容后面的一部分
     */
    public abstract String getSelect(Map<String,Object> map, List<Object> list);


    /**
     * 咳咳，一顿操作猛如虎，一看解决，用处不大，表的数量太少
     * insert语句，插入时直接插入一行
     * @param object
     */
    @Override
    public void insert(T object) throws IllegalAccessException, SQLException {
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        // insert语句如下：insert into tableName (字段名，字段名) value('值','值');
        String base = "insert {0} ({1}) value({2})";
        // 要防止注入问题
        String sql = MessageFormat.format(base,getTableName(),LinkSqlUtil.insertLinkSql(object),LinkSqlUtil.insertValueLinkSql(object));
        System.out.println(sql);
        List<Object> list = new ArrayList();
        LinkSqlUtil.pojoList(list,object);
        queryRunner.update(sql,list.toArray());
    }

    /**
     * update语句
     * 可以直接修改整一行数据
     * update 表名 set 列名=值，列名=值 where 条件
     */
    @Override
    public void update(T object) throws IllegalAccessException, NoSuchFieldException, SQLException {
        try {
            QueryRunner queryRunner = new QueryRunner();
            // 先写有占位符的SQL语句
            String base = "update {0} set {1} where {2}";
            List<Object> objectList = new ArrayList<>();
            String sql = MessageFormat.format(base, getTableName(), LinkSqlUtil.updateSetLinkSql(object), getQueryCondition(object, objectList));
            System.out.println(sql);
            List<Object> list = new ArrayList<>();
            LinkSqlUtil.pojoList(list, object);
            /**
             * 只能将手动将两个list集合连在一起了
             */
            LinkSqlUtil.listAndList(list, objectList);
            System.out.println(list);
            queryRunner.update(JdbcUtil.getConnection(), sql, list.toArray());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 局部更新，且另外一部分会发生变化
     * @param map
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SQLException
     */
    @Override
    public void update(Map<String,Object> map,T object) throws IllegalAccessException, NoSuchFieldException, SQLException {
        try {
            QueryRunner queryRunner = new QueryRunner();
            // 先写有占位符的SQL语句
            String base = "update {0} set {1} where {2}";
            List<Object> objectList = new ArrayList<>();
            String sql = MessageFormat.format(base, getTableName(), LinkSqlUtil.updateMapLinkSql(map), getQueryCondition(object, objectList));
            System.out.println(sql);
            List<Object> list = new ArrayList<>();
            LinkSqlUtil.mapList(list, map);
            LinkSqlUtil.listAndList(list, objectList);
            System.out.println(list);
            queryRunner.update(JdbcUtil.getConnection(), sql, list.toArray());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 一次更新一堆
     * @param mapOne
     * @param mapTwo
     */
    public void update (Map<String, Object> mapOne, Map<String, Object> mapTwo) throws Exception {
        QueryRunner queryRunner = new QueryRunner();
        // 先写有占位符的SQL语句
        String base = "update {0} set {1} where {2}";
        List<Object> objectList = new ArrayList<>();
        String sql = MessageFormat.format(base, getTableName(), LinkSqlUtil.updateMapLinkSql(mapOne), LinkSqlUtil.mapWhereLinkSql(mapTwo));
        System.out.println(sql);
        List<Object> list = new ArrayList<>();
        LinkSqlUtil.mapList(list, mapOne);
        LinkSqlUtil.mapList(objectList,mapTwo);
        LinkSqlUtil.listAndList(list, objectList);
        System.out.println(list);
        queryRunner.update(JdbcUtil.getConnection(), sql, list.toArray());
    }


    /**
     * 删除操作主要是依靠表的某一行数据，原先依靠id删除，局限太大
     * @param object
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    @Override
    public void delete(T object) throws NoSuchFieldException, IllegalAccessException, SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String base = "delete from {0} where {1};";
        String sql = MessageFormat.format(base,getTableName(),LinkSqlUtil.deleteWhereLinkSql(object,getTableName()));
        System.out.println(sql);
        queryRunner.update(JdbcUtil.getConnection(),sql);
    }

    /**
     * 删除操作主要是依靠表的某一行数据，原先依靠id删除，局限太大
     * @param map
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    @Override
    public void delete(Map<String,Object> map) throws NoSuchFieldException, IllegalAccessException, SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String base = "delete from {0} where {1};";
        String sql = MessageFormat.format(base,getTableName(),LinkSqlUtil.mapWhereLinkSql(map));
        System.out.println(sql);
        List<Object> list = new ArrayList<>();
        LinkSqlUtil.mapList(list,map);
        for(int i = 0;i < list.toArray().length;i++){
            System.out.println(list.toArray()[i]);
        }
        queryRunner.update(JdbcUtil.getConnection(),sql,list.toArray());
    }


    /**
     * 查询所有字段
     * @return 返回第一行查询结果的map集合
     * @throws SQLException
     */
    @Override
    public Map<String, Object> select(T object) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
        String base = "select * from {0} where {1};";
        List<Object> list = new ArrayList<>();
        String sql = MessageFormat.format(base,getTableName(),getQueryCondition(object,list));
        System.out.println(sql);
        System.out.println(list);
        return queryRunner.query(sql,list.toArray(),new MapHandler());
    }

    /**
     * map中有相关参数，可供判断如何查询
     * @return 返回所有查询结果的list集合
     * @throws SQLException
     */
    @Override
    public List<Map<String,Object>> select(Map<String,Object> map) throws Exception {
        try {
            QueryRunner queryRunner = new QueryRunner(JdbcUtil.getDataSource());
            String base = "select * from {0} where {1};";
            List<Object> list = new ArrayList<>();
            String sql = MessageFormat.format(base, getTableName(), getSelect(map, list));
            System.out.println(sql);
            System.out.println(list);
            return queryRunner.query(sql, list.toArray(), new MapListHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
