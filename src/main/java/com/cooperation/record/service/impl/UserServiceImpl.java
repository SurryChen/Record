package com.cooperation.record.service.impl;

import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.dao.UserDao;
import com.cooperation.record.dao.impl.UserDaoImpl;
import com.cooperation.record.domain.pojo.User;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.service.UserService;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户业务
 * @author cyl
 * @date 2021/10/11
 */
public class UserServiceImpl implements UserService {

    public static final UserDao userDao = new UserDaoImpl();

    @Override
    public ApiMsg register(User user) throws Exception {
        Map<String, Object> userMap = userDao.select(user);
        user.setPicture("http://192.168.43.121:8080/picture/xiong.jpg");
        System.out.println(userMap);
        // 该邮箱没有被注册
        if(userMap == null){
            userDao.insert(user);
            return new ApiMsg(ApiInfo.REGISTER_SUCCESS);
        } else {
            return new ApiMsg(ApiInfo.MAILBOX_FOUND);
        }
    }

    @Override
    public ApiMsg login(User user) throws Exception {
        ApiMsg apiMsg = null;
        // 先判断该邮箱是否已经注册
        Map<String,Object> map = userDao.select(user);
        // 为空就是没有注册
        if(map == null){
            apiMsg = new ApiMsg(ApiInfo.MAILBOX_NOTFOUND);
        } else {
            // 再判断该邮箱密码是否正确
            System.out.println(map.get("password"));
            if(user.getPassword().equals(map.get("password"))){
                apiMsg = new ApiMsg(ApiInfo.LOGIN_SUCCESS);
            } else {
                apiMsg = new ApiMsg(ApiInfo.PASSWORD_WRONG);
            }
        }
        return apiMsg;
    }

    @Override
    public ApiMsg mailModifyPassword(User user) throws Exception {
        // 通过邮箱，更新一下密码
        // 判断邮箱是否有注册
        Map<String,Object> map = userDao.select(user);
        if(map == null){
            return new ApiMsg(ApiInfo.MAILBOX_NOTFOUND);
        } else {
            Map<String,Object> mapUpdate = new HashMap<>();
            mapUpdate.put("password",user.getPassword());
            userDao.update(mapUpdate,user);
            return new ApiMsg(ApiInfo.PASSWORD_MODIFY_SUCCESS);
        }
    }

    @Override
    public ApiMsg getUser(User user) throws Exception {
        Map<String,Object> map = userDao.select(user);
        BeanUtils.populate(user,map);
        user.setPassword(null);
        user.setOtherPassword(null);
        ApiMsg<User> apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(user);
        return apiMsg;
    }


    @Override
    public ApiMsg updateUser(Map<String,Object> map) throws Exception {
        // 如果是密码，则先判断密码是否正确，然后再执行修改操作
        // 如果不是密码，直接执行修改操作
        // 先拿到id才能进行操作
        String userId = (String)map.get("userId");
        // 初始化一个user
        User user = new User();
        user.setUserId(Integer.parseInt(userId));
        if(map.get("password") != null){
            // 先根据id查询出对象，然后再比较
            Map<String,Object> mapk = userDao.select(user);
            BeanUtils.populate(user,mapk);
            if (!user.getPassword().equals(map.get("before"))){
                return new ApiMsg(ApiInfo.PASSWORD_WRONG);
            }
            // 移除不必要的键值对
            map.remove("before");
        }
        map.remove("update");
        userDao.update(map,user);
        return new ApiMsg(ApiInfo.SUCCESS);
    }


}
