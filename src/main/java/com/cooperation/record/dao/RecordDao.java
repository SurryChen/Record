package com.cooperation.record.dao;

import com.cooperation.record.domain.pojo.Record;
import com.cooperation.record.domain.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 查询
 * @author cyl
 * @date 2021/10/16
 */
public interface RecordDao {
    /**
     * 创建一篇记录
     * @param record
     * @throws Exception
     */
    public void insert(Record record) throws Exception;

    /**
     * 更新某一篇记录信息
     * @param record
     * @throws Exception
     */
    public void update(Record record) throws Exception;

    /**
     * 更新某一篇记录信息
     * @param map
     * @throws Exception
     */
    public void update(Map<String, Object> map, Record record) throws Exception;

    /**
     * 一次性更新多篇信息
     */
    public void update(Map<String, Object> mapOne, Map<String, Object> mapTwo) throws Exception;


    /**
     * 删除一篇记录
     * @param record
     * @throws Exception
     */
    public void delete(Record record) throws Exception;

    /**
     * 删除多篇记录
     */
    public void delete(Map<String, Object> map) throws Exception;

    /**
     * 更新用户信息
     * @param map
     * @param record
     * @throws Exception
     */
    /*public void update(Map<String,Object> map, Record record) throws Exception;*/

    /**
     * 获取某一篇文章信息
     * @throws Exception
     * @return 返回一个record对象的list
     */
    /*public Map<String,Object> select(Record record) throws Exception;*/

    /**
     * 获取用户信息
     * @throws Exception
     * @return 返回多个record对象的list
     */
    public List<Map<String,Object>> select(Map<String,Object> map) throws Exception;
}
