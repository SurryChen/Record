package com.cooperation.record.dao.impl;

import com.cooperation.record.dao.Mapper.SimpleBaseMapper;
import com.cooperation.record.dao.DayPlanDao;
import com.cooperation.record.domain.pojo.DayPlan;

import java.util.List;
import java.util.Map;

/**
 * @author cyl
 * @date 2021/10/22
 */
public class DayPlanDaoImpl extends SimpleBaseMapper<DayPlan> implements DayPlanDao {

    @Override
    public String getTableName() {
        return "dayPlan";
    }

    @Override
    public String getQueryCondition(DayPlan object, List<Object> list) {
        list.add(object.getPlanProjectId());
        list.add(object.getPlanDate());
        return "planProjectId = ? and "+"planDate = ?";
    }

    /**
     * 都是返回唯一标识
     * @param map
     * @return
     */
    @Override
    public String getSelect(Map<String, Object> map, List<Object> list) {
        if(map.get("planProjectId") != null && map.get("planDate") != null){
            list.add(map.get("planProjectId"));
            list.add(map.get("planDate"));
            return "planProjectId = ?"
                    +" and planDate = ?";
        } else if(map.get("planProjectId") != null){
            list.add(map.get("planProjectId"));
            return "planProjectId = ?";
        } else if(map.get("planDate") != null && map.get("planProjectType") != null){
            list.add(map.get("planDate"));
            list.add(map.get("planProjectType"));
            list.add(map.get("userId"));
            return "planDate = ? and planProjectType = ? AND planProjectId = ANY(SELECT planProjectId FROM planproject WHERE userId = ?)";
        } else if(map.get("planDate") != null){
            list.add(map.get("planDate"));
            list.add(map.get("userId"));
            return "planDate = ? AND planProjectId = ANY(SELECT planProjectId FROM planproject WHERE userId = ?)";
        }
        return null;
    }
}
