package com.cooperation.record.dao.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 定义一下Crud
 * 主要使用javabean和Map集合
 * 下面的方法也要抛出错误
 * @author cyl
 * @date 2021/10/09
 */
public interface BaseMapper<T> {
    /**
     * 插入
     * @param object
     */
    public abstract void insert(T object) throws Exception;

    /**
     * 删除
     * @param object
     */
    public abstract void delete(T object) throws Exception;

    /**
     * 删除
     * @param map
     */
    public abstract void delete(Map<String, Object> map) throws Exception;

    /**
     * 更新
     * @param object
     */
    public abstract void update(T object) throws Exception;

    /**
     * 更新
     * @param object
     */
    public abstract void update(Map<String,Object> map,T object) throws Exception;

    /**
     * 查询
     * @return 返回一个javabean的list集合
     */
    public abstract Map<String, Object> select(T object) throws Exception;

    /**
     * 查询返回一堆对象
     * @return 返回一堆
     */
    public abstract List<Map<String,Object>> select(Map<String,Object> map) throws Exception;
}
