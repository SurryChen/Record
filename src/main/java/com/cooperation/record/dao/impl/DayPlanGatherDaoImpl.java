package com.cooperation.record.dao.impl;

import com.cooperation.record.dao.DayPlanGatherDao;
import com.cooperation.record.dao.Mapper.SimpleBaseMapper;
import com.cooperation.record.domain.pojo.DayPlanGather;

import java.util.List;
import java.util.Map;

/**
 * @author cyl
 * @date 2021/10/22
 */
public class DayPlanGatherDaoImpl extends SimpleBaseMapper<DayPlanGather> implements DayPlanGatherDao {

    @Override
    public String getTableName() {
        return "dayPlanGather";
    }

    @Override
    public String getQueryCondition(DayPlanGather object, List<Object> list) {
        list.add(object.getUserId());
        list.add(object.getPlanDate());
        return "userId = ? and "+"planDate = ?";
    }

    /**
     * 都是返回唯一标识
     * @param map
     * @return
     */
    @Override
    public String getSelect(Map<String, Object> map, List<Object> list) {
        if(map.get("userId") != null && map.get("planDate") != null){
            list.add(map.get("userId"));
            list.add(map.get("planDate"));
            return "userId = ?"
                    +" and planDate = ?";
        } else if(map.get("userId") != null){
            list.add(map.get("userId"));
            return "userId = ?";
        }
        return null;
    }

}
