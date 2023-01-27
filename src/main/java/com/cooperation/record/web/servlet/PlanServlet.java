package com.cooperation.record.web.servlet;

import com.alibaba.fastjson.JSONArray;
import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.core.ProxyFactory;
import com.cooperation.record.domain.pojo.DayPlan;
import com.cooperation.record.domain.pojo.DayPlanGather;
import com.cooperation.record.domain.pojo.PlanProject;
import com.cooperation.record.domain.pojo.User;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.domain.vo.TimeTable;
import com.cooperation.record.service.Factory.ServiceProxyFactory;
import com.cooperation.record.service.PlanService;
import com.cooperation.record.service.impl.PlanServiceImpl;
import com.cooperation.record.utils.ResponseUtil;
import com.cooperation.record.web.servlet.mvc.annotation.ServletHandler;
import com.cooperation.record.web.servlet.mvc.enums.RequestType;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与计划内容相关的接口
 * @author cyl
 * @date 2021/10/25
 */
@ServletHandler("/Plan")
public class PlanServlet {

    PlanService planService = ServiceProxyFactory.getPlanService();

    /**
     * 创建一个计划项目
     * @param request
     * @param response
     */
    @ServletHandler(value = "/establishPlanProject", type = RequestType.POST)
    public void establishPlanProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 初始化一个计划项目
        Map<String,String[]> map = request.getParameterMap();
        PlanProject planProject = new PlanProject();
        BeanUtils.populate(planProject,map);
        ApiMsg apiMsg = planService.establishPlanProject(planProject);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 删除一个计划项目
     * @param request
     * @param response
     */
    @ServletHandler(value = "/deletePlanProject", type = RequestType.DELETE)
    public void deletePlanProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 初始化一个计划项目
        Map<String,String[]> map = request.getParameterMap();
        PlanProject planProject = new PlanProject();
        BeanUtils.populate(planProject,map);
        ApiMsg apiMsg = planService.deletePlanProject(planProject);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 更新一个计划项目里面的内容
     * @param request
     * @param response
     */
    @ServletHandler(value = "/updatePlanProject", type = RequestType.PUT)
    public void updatePlanProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 初始化一个计划项目
        Map<String,String[]> map = request.getParameterMap();
        ApiMsg apiMsg = null;
        // 判断是否更新的部分内容
        String updatePart = request.getParameter("updatePart");
        if(updatePart != null) {
            // 拿到参数
            String planProjectId = request.getParameter("planProjectId");
            String planProjectSummary = request.getParameter("planProjectSummary");
            Map<String, Object> mapOther = new HashMap<>();
            mapOther.put("planProjectId",planProjectId);
            mapOther.put("planProjectSummary",planProjectSummary);
            // 更新参数
            apiMsg = planService.updatePlanProject(mapOther);
        } else {
            PlanProject planProject = new PlanProject();
            BeanUtils.populate(planProject,map);
            apiMsg = planService.updatePlanProject(planProject);
        }
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 更新一个计划项目里面的日计划的内容
     * @param request
     * @param response
     */
    @ServletHandler(value = "/updateDayPlan", type = RequestType.PUT)
    public void updateDayPlan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 初始化一个计划项目
        Map<String,String[]> map = request.getParameterMap();
        DayPlan dayPlan = new DayPlan();
        BeanUtils.populate(dayPlan,map);
        ApiMsg apiMsg = planService.updateDayPlan(dayPlan);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 更新一个计划项目里面的日计划的内容，更改了自定义计划的部分
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/updateDayPlanCustomize", type = RequestType.PUT)
    public void updateDayPlanCustomize (HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 初始化一个计划项目
        Map<String,String[]> map = request.getParameterMap();
        String update = request.getParameter("update");
        String operation = request.getParameter("operation");
        DayPlan dayPlan = new DayPlan();
        BeanUtils.populate(dayPlan,map);
        ApiMsg apiMsg = planService.updateDayPlanCustomize(dayPlan, update, operation);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个用户的周计划项目或自定义计划项目
     * 要获取到planProjectType
     * @param request
     * @param response
     */
    @ServletHandler(value = "/requirePlanProject", type = RequestType.GET)
    public void requirePlanProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // session中获取到一个user对象
        /**
         * 测试中，先手动创建一个user
         */
        /*HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");*/
        User user = new User();
        user.setUserId(4);
        // 还要知道是周计划还是自定义计划
        String type = request.getParameter("planProjectType");
        ApiMsg<List<PlanProject>> apiMsg = planService.requirePlanProject(user,type);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个计划项目中所有的日计划内容
     * @param request
     * @param response
     */
    @ServletHandler(value = "/requireDayPlan", type = RequestType.GET)
    public void requireDayPlan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 初始化一个计划项目
        Map<String,String[]> map = request.getParameterMap();
        PlanProject planProject = new PlanProject();
        BeanUtils.populate(planProject,map);
        ApiMsg<List<DayPlan>> apiMsg = planService.requireDayPlan(planProject);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 修改日计划的总结部分内容
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/updateDayPlanGather", type = RequestType.PUT)
    public void updateDayPlanGather(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,String[]> map = request.getParameterMap();
        DayPlanGather dayPlanGather = new DayPlanGather();
        BeanUtils.populate(dayPlanGather,map);
        ApiMsg apiMsg = planService.updateDayPlanGather(dayPlanGather);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取日计划的小结
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requireDayPlanDetails", type = RequestType.GET)
    public void requireDayPlanDetails(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,String[]> map = request.getParameterMap();
        DayPlanGather dayPlanGather = new DayPlanGather();
        BeanUtils.populate(dayPlanGather,map);
        ApiMsg apiMsg = planService.requireDayPlanDetails(dayPlanGather);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个日计划的内容，发送所有的计划项目的该日的日计划
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requireDayPlanSum", type = RequestType.GET)
    public void requireDayPlanSum(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,String[]> map = request.getParameterMap();
        String userId = request.getParameter("userId");
        DayPlan dayPlan = new DayPlan();
        BeanUtils.populate(dayPlan,map);
        ApiMsg apiMsg = planService.requireDayPlanSum(dayPlan,userId);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个日计划内容的不同计划项目的目标，做一个汇总
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requirePlanProjectGoal", type = RequestType.GET)
    public void requirePlanProjectGoal(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String planDate = request.getParameter("planDate");
        String userId = request.getParameter("userId");
        ApiMsg apiMsg = planService.requirePlanProjectGoal(planDate,userId);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 导入课表到计划当中
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requireTimeTable", type = RequestType.GET)
    public void requireTimeTable(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 获取要导入的课表信息
        Map<String, String[]> map = request.getParameterMap();
        TimeTable timeTable = new TimeTable();
        BeanUtils.populate(timeTable,map);
        // 传入service业务逻辑层做判断
        ApiMsg apiMsg = planService.requireTimeTable(timeTable);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个计划项目的总结内容
     */
    @ServletHandler(value = "/requirePlanProjectSummary", type = RequestType.GET)
    public void requirePlanProjectSummary (HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 测试时先写定数据
        int userId = 4;
        String planProjectId = request.getParameter("planProjectId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("planProjectId",Integer.parseInt(planProjectId));
        ApiMsg apiMsg = planService.requirePlanProjectSummary(map);
        if(apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个日计划的完成情况
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requireDayPlanAllCondition", type = RequestType.GET)
    public void requireDayPlanAllCondition (HttpServletRequest request, HttpServletResponse response) throws Exception {
        String planProjectId = request.getParameter("planProjectId");
        ApiMsg apiMsg = planService.requireDayPlanAllCondition(planProjectId);
        if(apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 获取一个计划项目的完成情况
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requirePlanProjectGoalAllCondition", type = RequestType.GET)
    public void requirePlanProjectGoalAllCondition (HttpServletRequest request, HttpServletResponse response) throws Exception {
        String planProjectId = request.getParameter("planProjectId");
        ApiMsg apiMsg = planService.requirePlanProjectGoalAllCondition(planProjectId);
        if(apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    /**
     * 保存和获取计划失败原因
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/reason", type = RequestType.GET)
    public void reason (HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String[]> map = request.getParameterMap();
            DayPlanGather dayPlanGather = new DayPlanGather();
            BeanUtils.populate(dayPlanGather, map);
            String operation = request.getParameter("operation");
            ApiMsg apiMsg = planService.season(dayPlanGather, operation);
            if (apiMsg == null) {
                apiMsg = new ApiMsg(ApiInfo.WRONG);
            }
            ResponseUtil.send(response, apiMsg);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取计划失败原因
     * @param request
     * @param response
     * @throws Exception
     */
    @ServletHandler(value = "/requirePlanProjectUnfinishedSeason", type = RequestType.GET)
    public void requirePlanProjectUnfinishedSeason (HttpServletRequest request, HttpServletResponse response) throws Exception {
        String planProjectId = request.getParameter("planProjectId");
        ApiMsg apiMsg = planService.requirePlanProjectUnfinishedSeason(planProjectId);
        if(apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }
}
