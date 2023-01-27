package com.cooperation.record.dao.impl;

import com.cooperation.record.dao.Mapper.SimpleBaseMapper;
import com.cooperation.record.dao.UserDao;
import com.cooperation.record.domain.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author cyl
 * @date 2021/10/09
 */
public class UserDaoImpl extends SimpleBaseMapper<User> implements UserDao {
    @Override
    public String getTableName() {
        return "user";
    }

    @Override
    public String getQueryCondition(User user, List<Object> list) {
        String base = new String();
        if(user.getUserId() != 0){
            list.add(user.getUserId());
            base = "userId = ?";
        } else if(user.getMailbox() != null){
            list.add(user.getMailbox());
            base = "mailbox= ?";
        }
        return base;
    }

    @Override
    public String getSelect(Map<String, Object> map, List<Object> list) {
        return null;
    }
}
