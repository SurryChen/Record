package com.cooperation.record.service;

import com.cooperation.record.domain.pojo.User;
import com.cooperation.record.domain.vo.ApiMsg;

import java.util.Map;

/**
 * 用户业务
 * @author cyl
 * @date 2021/10/11
 */
public interface UserService {

    /** 用户注册方法
     * @return*/
    public ApiMsg register(User user) throws Exception;

    /** 用户登录方法 */
    public ApiMsg login(User user) throws Exception;

    /** 用户找回密码找回密码方法 */
    public ApiMsg mailModifyPassword(User user) throws Exception;

    /** 获取用户信息的方法 */
    public ApiMsg getUser(User user) throws Exception;

    /** 更改用户信息的方法 */
    public ApiMsg updateUser(Map<String,Object> map) throws Exception;
}
