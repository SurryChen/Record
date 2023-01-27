package com.cooperation.record.dao;

import com.cooperation.record.domain.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 方法命名为BaseMapper里面的方法名，毕竟一个是继承，一个是实现
 * @author cyl
 * @date 2021/10/09
 */
public interface UserDao {

    /**
     * 创建用户
     * @param user
     * @throws Exception
     */
    public void insert(User user) throws Exception;

    /**
     * 更新用户信息
     * @param user
     * @throws Exception
     */
    public void update(User user) throws Exception;

    /**
     * 更新用户信息
     * @param user
     * @throws Exception
     */
    public void update(Map<String,Object> map,User user) throws Exception;

    /**
     * 获取用户信息
     * @throws Exception
     * @return 返回一个user对象的list
     */
    public Map<String,Object> select(User user) throws Exception;
}
