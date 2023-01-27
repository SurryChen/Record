package com.cooperation.record.service;

import com.cooperation.record.domain.pojo.DayPlan;
import com.cooperation.record.domain.pojo.DayPlanGather;
import com.cooperation.record.domain.pojo.PlanProject;
import com.cooperation.record.domain.pojo.User;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.domain.vo.TimeTable;

import java.util.List;
import java.util.Map;

/**
 * 操作跟计划有关的表
 * 有plan和planProject
 * @author cyl
 * @date 2021/10/22
 */
public interface PlanService {

    /**
     * 创建一个计划项目
     * @param planProject
     */
    public ApiMsg establishPlanProject(PlanProject planProject) throws Exception;

    /**
     * 删除一个计划项目
     * @param planProject
     */
    public ApiMsg deletePlanProject(PlanProject planProject) throws Exception;

    /**
     * 更新一个计划项目里面的内容
     * @param planProject
     */
    public ApiMsg updatePlanProject(PlanProject planProject) throws Exception;

    /**
     * 更新一个计划项目里面的内容
     * @param map
     */
    public ApiMsg updatePlanProject(Map<String, Object> map) throws Exception;

    /**
     * 更新一个计划项目里面的日计划的内容
     * @param dayPlan
     */
    public ApiMsg updateDayPlan(DayPlan dayPlan) throws Exception;

    /**
     * 获取一个用户的周计划项目或自定义计划项目
     * @param user
     */
    public ApiMsg<List<PlanProject>> requirePlanProject(User user, String type) throws Exception;

    /**
     * 获取一个计划项目中所有的日计划内容
     * @param planProject
     */
    public ApiMsg<List<DayPlan>> requireDayPlan(PlanProject planProject) throws Exception;

    public ApiMsg updateDayPlanGather(DayPlanGather dayPlanGather) throws Exception;

    public ApiMsg requireDayPlanDetails(DayPlanGather dayPlanGather) throws Exception;

    public ApiMsg requireDayPlanSum(DayPlan dayPlan,String userId) throws Exception;

    public ApiMsg requirePlanProjectGoal(String planDate,String userId) throws Exception;

    /**
     * 导入课表的方法
     * @param timeTable
     * @return
     */
    ApiMsg requireTimeTable(TimeTable timeTable) throws Exception;

    /**
     * 获取计划项目总结内容
     * @param map
     * @return
     */
    ApiMsg requirePlanProjectSummary(Map<String, Object> map) throws Exception;

    /**
     * 获取一个计划项目中每日计划的完成情况
     * @param planProjectId
     * @return
     */
    ApiMsg requireDayPlanAllCondition(String planProjectId) throws Exception;

    /**
     * 修改自定义计划中的内容
     * @param dayPlan
     * @param update
     * @return
     */
    ApiMsg updateDayPlanCustomize(DayPlan dayPlan, String update, String operation) throws Exception;

    /**
     * 获取一个计划项目的目标完成情况
     * @param planProjectId
     * @return
     */
    ApiMsg requirePlanProjectGoalAllCondition(String planProjectId) throws Exception;

    /**
     * 保存和获取原因
     * @param dayPlanGather
     * @param operation
     * @return
     */
    ApiMsg season(DayPlanGather dayPlanGather, String operation) throws Exception;

    /**
     * 获取失败原因
     * @param planProjectId
     * @return
     */
    ApiMsg requirePlanProjectUnfinishedSeason(String planProjectId) throws Exception;
}
