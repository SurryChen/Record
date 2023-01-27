package com.cooperation.record.dao;

import com.cooperation.record.domain.pojo.DayPlan;

import java.util.List;
import java.util.Map;

/**
 * 定义对plan表操作的接口
 * @author cyl
 * @date 2021/10/22
 */
public interface DayPlanDao {

    /**
     * 增加一个日计划
     */
    public void insert(DayPlan dayPlan) throws Exception;

    /**
     * 删除日计划
     * 需要使用日期与计划项目id
     */
    public void delete(Map<String,Object> map) throws Exception;



    /**
     * 修改日计划内容
     * 需要使用到日期，在getQueryCondition()里面设置一下就可以了
     */
    public void update(DayPlan dayPlan) throws Exception;

    /**
     * 修改日计划内容
     * 需要使用到日期，在getQueryCondition()里面设置一下就可以了
     */
    public void update(Map<String, Object> map, DayPlan dayPlan) throws Exception;

    /**
     * 根据userId查询内容，并且可以设置getSelect()丰富一下功能
     */
    public List<Map<String,Object>> select(Map<String,Object> map) throws Exception;
}
