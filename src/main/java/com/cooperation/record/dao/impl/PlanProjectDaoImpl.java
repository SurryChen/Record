package com.cooperation.record.dao.impl;

import com.cooperation.record.dao.Mapper.SimpleBaseMapper;
import com.cooperation.record.dao.PlanProjectDao;
import com.cooperation.record.domain.pojo.PlanProject;

import java.util.List;
import java.util.Map;

/**
 * 操作跟数据库有关的表的dao实现类
 * @author cyl
 * @date 2021/10/22
 */
public class PlanProjectDaoImpl extends SimpleBaseMapper<PlanProject> implements PlanProjectDao {
    @Override
    public String getTableName() {
        return "planProject";
    }

    /**
     * 主要使用在update和select中
     * 不管是哪一个，因为传入的是一个对象，所以都是返回一个拼接的唯一标识的字符串
     * @param object
     * @return
     */
    @Override
    public String getQueryCondition(PlanProject object, List<Object> list) {
        if(object.getPlanProjectId() != 0){
            list.add(object.getPlanProjectId());
            return "planProjectId = ?";
        } else {
            System.out.println("返回了空");
            return null;
        }
    }

    @Override
    public String getSelect(Map<String, Object> map, List<Object> list) {
        if(map.get("userId") != null && map.get("planProjectType") != null){
            list.add(map.get("userId"));
            list.add(map.get("planProjectType"));
            return "userId = ?"+" and "+"planProjectType= ?";
        } else if(map.get("planDate") != null){
            list.add(map.get("userId"));
            list.add(map.get("planDate"));
            return "userId = ? and (? BETWEEN planProjectDateStart AND planProjectDateEnd)";
        } else if(map.get("userId") != null && map.get("planProjectId") != null) {
            list.add(map.get("userId"));
            list.add(map.get("planProjectId"));
            return "userId = ? and planProjectId = ?";
        } else if(map.get("planProjectId") != null) {
            list.add(map.get("planProjectId"));
            return "planProjectId = ?";
        }
        return null;
    }
}
