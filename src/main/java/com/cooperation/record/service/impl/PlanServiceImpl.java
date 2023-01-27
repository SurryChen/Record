package com.cooperation.record.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.dao.DayPlanDao;
import com.cooperation.record.dao.DayPlanGatherDao;
import com.cooperation.record.dao.PlanProjectDao;
import com.cooperation.record.dao.UserDao;
import com.cooperation.record.dao.impl.DayPlanDaoImpl;
import com.cooperation.record.dao.impl.DayPlanGatherDaoImpl;
import com.cooperation.record.dao.impl.PlanProjectDaoImpl;
import com.cooperation.record.dao.impl.UserDaoImpl;
import com.cooperation.record.domain.pojo.DayPlan;
import com.cooperation.record.domain.pojo.DayPlanGather;
import com.cooperation.record.domain.pojo.PlanProject;
import com.cooperation.record.domain.pojo.User;
import com.cooperation.record.domain.vo.*;
import com.cooperation.record.service.PlanService;
import com.cooperation.record.utils.CalendarUtil;
import com.cooperation.record.utils.CountNumberUtil;
import com.cooperation.record.utils.RequireTimeTable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.commons.beanutils.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PlanService接口的实现类
 * @author cyl
 * @date 2021/10/22
 */
public class PlanServiceImpl implements PlanService {

    DayPlanDao dayPlanDao = new DayPlanDaoImpl();
    PlanProjectDao planProjectDao = new PlanProjectDaoImpl();
    DayPlanGatherDao dayPlanGatherDao = new DayPlanGatherDaoImpl();
    UserDao userDao = new UserDaoImpl();

    /**
     * 创建一个计划项目
     * 1、如果是周计划，判断该计划是否已经存在（实际上，已经交由前端去判断了）
     * 2、如果是自定义计划，直接插入
     * 3、创建计划项目的同时，需要创建相应的日计划
     * 4、创建计划的时候无法同时创建日计划，因为计划项目的id还没有确定
     * @param planProject
     * @return
     */
    @Override
    public ApiMsg establishPlanProject(PlanProject planProject) throws Exception {
        planProject = CountNumberUtil.countPlanProject(planProject);
        if(planProject.getPlanProjectId() == 0){
            if(planProjectDao.select(planProject) == null){
                planProjectDao.insert(planProject);
            } else {
                return new ApiMsg(ApiInfo.WRONG);
            }
        } else {
            planProjectDao.update(planProject);
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    /**
     * 删除一个计划项目
     * 也要删除相应的日计划内容
     * @param planProject
     * @return
     */
    @Override
    public ApiMsg deletePlanProject(PlanProject planProject) throws Exception {
        Map<String,Object> map = new HashMap<>();
        // 放入id
        map.put("planProjectId",planProject.getPlanProjectId());
        System.out.println("准备设置一下日期");
        String[] date = CalendarUtil.getDateAfter(planProject.getPlanProjectDateStart(),
                CalendarUtil.getTwoDays(planProject.getPlanProjectDateStart(), planProject.getPlanProjectDateEnd()));
        // 放入日期后删除
        System.out.println("准备删除日期");
        System.out.println("date大小"+date.length);
        for(int i = 0;i < date.length;i++){
            map.put("planDate",date[i]);
            System.out.println(date[i]);
            dayPlanDao.delete(map);
        }
        // 删除项目
        System.out.println("准备删除项目");
        planProjectDao.delete(planProject);
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    /**
     * 更新一个项目的内容
     * @param planProject
     * @return
     */
    @Override
    public ApiMsg updatePlanProject(PlanProject planProject) throws Exception {
        planProject = CountNumberUtil.countPlanProject(planProject);
        Map<String, Object> map = new HashMap<>();
        map.put("planProjectId",planProject.getPlanProjectId());
        map.put("planProjectType",planProject.getPlanProjectType());
        map.put("userId",planProject.getUserId());
        map.put("planProjectName",planProject.getPlanProjectName());
        map.put("planProjectDateStart",planProject.getPlanProjectDateStart());
        map.put("planProjectDateEnd",planProject.getPlanProjectDateEnd());
        map.put("planProjectCreateTime",planProject.getPlanProjectCreateTime());
        map.put("planProjectUpdateTime",planProject.getPlanProjectUpdateTime());
        map.put("planProjectGoal",planProject.getPlanProjectGoal());
        map.put("planFinished",planProject.getPlanFinished());
        map.put("planUnfinished",planProject.getPlanUnfinished());
        planProjectDao.update(map,planProject);
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    @Override
    public ApiMsg updatePlanProject(Map<String, Object> map) throws Exception {
        try {
            PlanProject planProject = new PlanProject();
            planProject.setPlanProjectId(Integer.parseInt((String) map.get("planProjectId")));
            map.remove("planProjectId");
            planProjectDao.update(map, planProject);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    /**
     * 更新一个计划项目中日计划的内容
     * @param dayPlan
     * @return
     */
    @Override
    public ApiMsg updateDayPlan(DayPlan dayPlan) throws Exception {
        // 先判断这个日计划是否存在
        Map<String,Object> map = new HashMap<>();
        map.put("planProjectId",dayPlan.getPlanProjectId());
        map.put("planDate",dayPlan.getPlanDate());
        List<Map<String, Object>> list = dayPlanDao.select(map);
        // 存在就更新
        try {
            dayPlan = CountNumberUtil.countDayPlan(dayPlan);
            if (!list.isEmpty()) {
                dayPlanDao.update(dayPlan);
            } else {
                dayPlanDao.insert(dayPlan);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    /**
     * 更新一个计划项目中日计划的内容
     * @param dayPlan
     * @return
     */
    @Override
    public ApiMsg updateDayPlanCustomize(DayPlan dayPlan, String update, String operation) throws Exception {
        // 先判断这个日计划是否存在
        Map<String,Object> map = new HashMap<>();
        map.put("planProjectId",dayPlan.getPlanProjectId());
        map.put("planDate",dayPlan.getPlanDate());
        List<Map<String, Object>> list = dayPlanDao.select(map);
        System.out.println("-----------------------------");
        System.out.println(operation);
        System.out.println("-----------------------------");
        try {
            if ("0".equals(operation)) {
                System.out.println("进去了");
                // 删除
                /**
                 * 获取到原来的全部，然后匹配，更改后更新
                 */
                Map<String, Object> objectMap = list.get(0);
                // 这个是原本的
                String planContentOne = (String) objectMap.get("planContent");
                // 这个需要更改的
                String planContentTwo = dayPlan.getPlanContent();
                // 将planContent转换成JSON数据格式
                JSONArray jsonArrayOne = JSON.parseArray(planContentOne);
                JSONArray jsonArrayTwo = JSON.parseArray(planContentTwo);
                if (jsonArrayTwo.isEmpty()) {
                    // 如果空，直接替换更新
                    dayPlan.setPlanContent(update);
                    dayPlanDao.update(dayPlan);
                } else {
                    JSONObject jsonObject = jsonArrayTwo.getJSONObject(0);
                    for (int i = 0; i < jsonArrayOne.size(); i++) {
                        JSONObject midden = jsonArrayOne.getJSONObject(i);
                        // 判断是不是这一项
                        // "planProjectId":"21","startTime":"","endTime":"","content":"999999999","condition":"否"
                        if (midden.get("planProjectId").equals(jsonObject.get("planProjectId")) &&
                                midden.get("startTime").equals(jsonObject.get("startTime")) &&
                                midden.get("endTime").equals(jsonObject.get("endTime")) &&
                                midden.get("content").equals(jsonObject.get("content")) &&
                                midden.get("condition").equals(jsonObject.get("condition"))) {
                            jsonArrayOne.remove(i);
                            System.out.println("-----------------");
                            System.out.println(jsonArrayOne);
                            System.out.println("-----------------");
                            break;
                        }
                    }
                    dayPlan.setPlanContent(jsonArrayOne.toJSONString());
                    try {
                        dayPlan = CountNumberUtil.countDayPlan(dayPlan);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dayPlanDao.update(dayPlan);
                }
            } else if ("1".equals(operation)) {
                // 修改
                /**
                 * 获取到原来的全部，然后匹配，更改后更新
                 */
                Map<String, Object> objectMap = list.get(0);
                // 这个是原本的
                String planContentOne = (String) objectMap.get("planContent");
                // 这个需要更改的
                String planContentTwo = dayPlan.getPlanContent();
                // 将planContent转换成JSON数据格式
                JSONArray jsonArrayOne = JSON.parseArray(planContentOne);
                System.out.println("planContentTwo:" + planContentTwo);
                JSONArray jsonArrayTwo = JSON.parseArray(planContentTwo);
                if (jsonArrayTwo.isEmpty()) {
                    // 如果空，直接替换更新
                    dayPlan.setPlanContent(update);
                    try {
                        dayPlan = CountNumberUtil.countDayPlan(dayPlan);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dayPlanDao.update(dayPlan);
                } else {
                    JSONObject jsonObject = jsonArrayTwo.getJSONObject(0);
                    for (int i = 0; i < jsonArrayOne.size(); i++) {
                        JSONObject midden = jsonArrayOne.getJSONObject(i);
                        // 判断是不是这一项
                        // "planProjectId":"21","startTime":"","endTime":"","content":"999999999","condition":"否"
                        if (midden.get("planProjectId").equals(jsonObject.get("planProjectId")) &&
                                midden.get("startTime").equals(jsonObject.get("startTime")) &&
                                midden.get("endTime").equals(jsonObject.get("endTime")) &&
                                midden.get("content").equals(jsonObject.get("content")) &&
                                midden.get("condition").equals(jsonObject.get("condition"))) {
                            jsonArrayOne.remove(i);
                            JSONArray jsonArray = JSON.parseArray(update);
                            JSONObject jsonObjectOther = jsonArray.getJSONObject(0);
                            jsonArrayOne.add(jsonObjectOther);
                            break;
                        }
                    }
                    dayPlan.setPlanContent(jsonArrayOne.toJSONString());
                    try {
                        dayPlan = CountNumberUtil.countDayPlan(dayPlan);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dayPlanDao.update(dayPlan);
                }
            } else if ("2".equals(operation)) {
                // 增加
                /**
                 * 获取到原来的全部，然后匹配，更改后更新
                 */
                if (list.isEmpty()) {
                    try {
                        dayPlan = CountNumberUtil.countDayPlan(dayPlan);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dayPlanDao.insert(dayPlan);
                    return null;
                }
                Map<String, Object> objectMap = list.get(0);
                // 这个是原本的
                String planContentOne = (String) objectMap.get("planContent");
                // 这个需要更改的
                String planContentTwo = dayPlan.getPlanContent();
                // 将planContent转换成JSON数据格式
                JSONArray jsonArrayOne = JSON.parseArray(planContentOne);
                System.out.println("planContentTwo:" + planContentTwo);
                JSONArray jsonArrayTwo = JSON.parseArray(planContentTwo);
                if (jsonArrayTwo.isEmpty()) {
                    // 如果空，直接替换更新
                    dayPlan.setPlanContent(update);
                    try {
                        dayPlan = CountNumberUtil.countDayPlan(dayPlan);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dayPlanDao.update(dayPlan);
                } else {
                    // 需要增加的jsonObject对象
                    JSONObject jsonObject = jsonArrayTwo.getJSONObject(0);
                    jsonArrayOne.add(jsonObject);
                    dayPlan.setPlanContent(jsonArrayOne.toJSONString());
                    try {
                        dayPlan = CountNumberUtil.countDayPlan(dayPlan);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dayPlanDao.update(dayPlan);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    @Override
    public ApiMsg requirePlanProjectGoalAllCondition(String planProjectId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("planProjectId",planProjectId);
        List<Map<String, Object>> list = planProjectDao.select(map);
        List listMidden = new ArrayList();
        for (int i = 0;i < list.size();i++) {
            Map<String, Object> midden = list.get(i);
            PlanProject planProject = new PlanProject();
            BeanUtils.populate(planProject,midden);
            PlanProjectCondition planProjectCondition = new PlanProjectCondition(planProject);
            listMidden.add(planProjectCondition);
        }
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(listMidden);
        return apiMsg;
    }

    /**
     * 保存和获取原因
     * @param dayPlanGather
     * @param operation
     * @return
     */
    @Override
    public ApiMsg season(DayPlanGather dayPlanGather, String operation) throws Exception {
        // 判断保存还是获取
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        if("0".equals(operation)) {
             // 0是保存
            Map<String, Object> map = new HashMap<>();
            map.put("planDate",dayPlanGather.getPlanDate());
            map.put("userId",dayPlanGather.getUserId());
            List<Map<String, Object>> list = dayPlanGatherDao.select(map);
            Map<String, Object> midden = list.get(0);
            dayPlanGather.setPlanSummary((String) midden.get("planSummary"));
            dayPlanGatherDao.update(dayPlanGather);
        } else if ("1".equals(operation)) {
            // 1是获取
            Map<String, Object> map = new HashMap<>();
            map.put("planDate",dayPlanGather.getPlanDate());
            map.put("userId",dayPlanGather.getUserId());
            List<Map<String, Object>> list = dayPlanGatherDao.select(map);
            Map<String, Object> midden = list.get(0);
            dayPlanGather.setReason((String) midden.get("reason"));
            apiMsg.setData(dayPlanGather);
        }
        return apiMsg;
    }

    /**
     * 获取失败原因
     * @param planProjectId
     * @return
     */
    @Override
    public ApiMsg requirePlanProjectUnfinishedSeason(String planProjectId) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("planProjectId", planProjectId);
            // 拿到计划项目的内容
            List<Map<String, Object>> list = planProjectDao.select(map);
            // 获取到日期
            Map<String, Object> planProjectMap = list.get(0);
            // 将日期Date类转成String
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(planProjectMap.get("planProjectDateStart"));
            String end = sdf.format(planProjectMap.get("planProjectDateEnd"));
            // 获取从开始日期到结束日期这一段时间的所有日期
            int dateLength = CalendarUtil.getTwoDays(start, end);
            String[] date = CalendarUtil.getDateAfter(start, dateLength);
            int userId = (Integer)planProjectMap.get("userId");
            // 开始循环找到计划原因
            Map<String, Object> midden = new HashMap<>();
            midden.put("userId", userId);
            UnfinishedReason unfinishedReason = new UnfinishedReason();
            for (int i = 0; i < date.length; i++) {
                // 找到某一个天的原因
                midden.put("planDate", date[i]);
                List<Map<String, Object>> gather = dayPlanGatherDao.select(midden);
                Map<String, Object> dayPlanGatherMap = gather.get(0);
                // 拿到原因
                String reason = (String) dayPlanGatherMap.get("reason");
                // 统计原因
                unfinishedReason.add(reason);
            }
            unfinishedReason.num();
            ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
            apiMsg.setData(unfinishedReason);
            return apiMsg;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个用户的计划项目
     * 需要知道是日计划还是自定义计划
     * @param user
     * @return
     */
    @Override
    public ApiMsg<List<PlanProject>> requirePlanProject(User user, String type) throws Exception {
        // 保存转换结果
        List<PlanProject> list = new ArrayList<>();
        // 准备查找的条件
        Map<String,Object> map = new HashMap<>();
        map.put("userId",user.getUserId());
        map.put("planProjectType",type);
        // 开始查找
        List<Map<String,Object>> maps = planProjectDao.select(map);
        // 根据查找的内容，转换成javaBean对象
        try {
            for (int i = 0; i < maps.size(); i++) {
                Map<String, Object> beanMap = maps.get(i);
                PlanProject planProject = new PlanProject();
                BeanUtils.populate(planProject, beanMap);
                list.add(planProject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 发送传输转换结果
        ApiMsg<List<PlanProject>> apiMsg = new ApiMsg<>(ApiInfo.SUCCESS);
        apiMsg.setData(list);
        return apiMsg;
    }

    /**
     * 获取一个计划项目中的每日计划内容
     * @param planProject
     * @return
     */
    @Override
    public ApiMsg<List<DayPlan>> requireDayPlan(PlanProject planProject) throws Exception {
        // 需要的就是planProjectId
        // 查询的就是dayPlan表，使用map集合查询
        // 准备查找条件
        Map<String,Object> map = new HashMap<>();
        map.put("planProjectId",planProject.getPlanProjectId());
        // 查找
        List<Map<String,Object>> list = dayPlanDao.select(map);
        // 查找出来的内容准备转换
        List<DayPlan> listDayPlan = new ArrayList<>();
        // 将map的list集合转换成dayPlan的list集合
        for(int i = 0;i < list.size();i++){
            Map<String, Object> mapOther = list.get(i);
            DayPlan dayPlan = new DayPlan();
            BeanUtils.populate(dayPlan,mapOther);
            listDayPlan.add(dayPlan);
        }
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(listDayPlan);
        return apiMsg;
    }

    /**
     * 获取计划总结
     * @param dayPlanGather
     * @return
     * @throws Exception
     */
    @Override
    public ApiMsg updateDayPlanGather(DayPlanGather dayPlanGather) throws Exception {
        // 先判断这个日计划是否存在
        Map<String,Object> map = new HashMap<>();
        map.put("userId",dayPlanGather.getUserId());
        map.put("planDate",dayPlanGather.getPlanDate());
        List<Map<String, Object>> list = dayPlanGatherDao.select(map);
        // 存在就更新
        try {
            if (!list.isEmpty()) {
                dayPlanGatherDao.update(dayPlanGather);
            } else {
                dayPlanGatherDao.insert(dayPlanGather);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    /**
     * 获取一个用户某一天的计划总结
     * @param dayPlanGather
     * @return
     * @throws Exception
     */
    @Override
    public ApiMsg requireDayPlanDetails(DayPlanGather dayPlanGather) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",dayPlanGather.getUserId());
        map.put("planDate",dayPlanGather.getPlanDate());
        List<Map<String, Object>> list = dayPlanGatherDao.select(map);
        if(list.isEmpty()){
            return new ApiMsg(ApiInfo.SUCCESS);
        }
        Map<String,Object> mapOther = list.get(0);
        BeanUtils.populate(dayPlanGather,mapOther);
        ApiMsg<DayPlanGather> apiMsg = new ApiMsg<DayPlanGather>(ApiInfo.SUCCESS);
        apiMsg.setData(dayPlanGather);
        return apiMsg;
    }

    /**
     * 获取一个用户某一天的所有计划
     * @param dayPlan
     * @return
     * @throws Exception
     */
    @Override
    public ApiMsg requireDayPlanSum(DayPlan dayPlan,String userId) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("planDate",dayPlan.getPlanDate());
        map.put("userId",Integer.parseInt(userId));
        System.out.println("map集合里面的东西"+map);
        List<Map<String, Object>> list = dayPlanDao.select(map);
        List<DayPlan> dayPlanList = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            dayPlan = new DayPlan();
            Map<String,Object> mapOther = list.get(i);
            BeanUtils.populate(dayPlan,mapOther);
            dayPlanList.add(dayPlan);
        }
        ApiMsg<List<DayPlan>> apiMsg = new ApiMsg<>(ApiInfo.SUCCESS);
        apiMsg.setData(dayPlanList);
        return apiMsg;
    }

    /**
     * 获取用户某一天的计划项目
     * @param planDate
     * @return
     * @throws Exception
     */
    @Override
    public ApiMsg requirePlanProjectGoal(String planDate,String userId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("planDate",planDate);
        map.put("userId",Integer.parseInt(userId));
        List<Map<String,Object>> list = planProjectDao.select(map);
        ApiMsg<List<Map<String,Object>>> apiMsg = new ApiMsg<>(ApiInfo.SUCCESS);
        apiMsg.setData(list);
        return apiMsg;
    }

    /**
     * 获取课表信息的方法
     * (1)根据userId找到账号绑定的信息
     * (2)获取到今天的日期，获取本周开始日期，下周开始日期，获取到该周计划的内容
     * (3)根据term和week获得到课表.xlsx去下载分析里面的信息，封装成一个JSONObject对象
     * (4)将两部分和在一起，然后保存进数据库
     * 但是还需统计
     * @param timeTable
     * @return
     */
    @Override
    public ApiMsg requireTimeTable(TimeTable timeTable) throws Exception {
        // (1)根据userId找到账号绑定的信息
        User user = new User();
        user.setUserId(timeTable.getUserId());
        Map<String, Object> map = userDao.select(user);
        String otherAccount = (String)map.get("otherAccount");
        String otherPassword = (String)map.get("otherPassword");
        // (1).1如果为空，停止操作，返回参数缺失
        if(otherAccount == null || otherPassword == null) {
            return new ApiMsg(ApiInfo.WRONG);
        }
        // (2)获取到今天的日期，获取本周开始日期，下周开始日期，获取到该周计划的内容
        // (2).1获取计划的开始和结束日期
        String start = null;
        if(timeTable.getWhen() == 0) {
            // 本周计划
            start = CalendarUtil.getThisMonDate();
        } else {
            // 下周计划
            start = CalendarUtil.getNextMonDate();
        }
        // 获取周计划中每一天的日期
        String[] dates = CalendarUtil.getDateAfter(start,6);
        // (2).2根据日期查询计划内容，查询的是日计划的内容，并且是周计划的日计划内容
        Map<String, Object> mapPlan = new HashMap<>();
        List<DayPlan> list = new ArrayList<>();
        mapPlan.put("userId",timeTable.getUserId());
        mapPlan.put("planProjectType",0);
        for(int i = 0;i < dates.length;i++) {
            mapPlan.put("planDate",dates[i]);
            List<Map<String, Object>> listDayPlan = dayPlanDao.select(mapPlan);
            DayPlan dayPlan = new DayPlan();
            if(listDayPlan.isEmpty()) {
                list.add(null);
            } else {
                BeanUtils.populate(dayPlan, listDayPlan.get(0));
                list.add(dayPlan);
            }
        }
        // (3)根据term和week获得到课表.xlsx去下载分析里面的信息，封装成一个JSONObject对象
        JSONObject jsonObjectTimeTable = RequireTimeTable.midden(otherAccount,otherPassword,
                timeTable.getTerm(),timeTable.getWeek());
        if(jsonObjectTimeTable == null) {
            return null;
        }
        // list里面有多天，只能使用日期来判断
        for(int i = 0;i < list.size();i++) {
            DayPlan dayPlan = list.get(i);
            // 修改content的内容，然后更新
            if(dayPlan == null) {
                continue;
            }
            String planContent = dayPlan.getPlanContent();
            // 添加方式就是，使用JSONArray转换一下对象，使用JSONObject添加对象最后再转换成字符串保存到数据库当中
            JSONArray jsonArray = JSONArray.parseArray(planContent);
            // 拿到几个JSONObject
            String content = null;
            if(i == 0) {
                content = jsonObjectTimeTable.get("星期一").toString();
            } else if (i == 1) {
                content = (String)jsonObjectTimeTable.get("星期二").toString();
            } else if (i == 2) {
                content = (String)jsonObjectTimeTable.get("星期三").toString();
            } else if (i == 3) {
                content = (String)jsonObjectTimeTable.get("星期四").toString();
            } else if (i == 4) {
                content = (String)jsonObjectTimeTable.get("星期五").toString();
            } else if (i == 5) {
                content = (String)jsonObjectTimeTable.get("星期六").toString();
            } else if (i == 6) {
                content = (String)jsonObjectTimeTable.get("星期日").toString();
            }
            JSONArray jsonArray1 = JSONArray.parseArray(content);
            for(int j = 0;j < jsonArray1.size();j++) {
                JSONObject jsonObject = jsonArray1.getJSONObject(j);
                jsonObject.put("planProjectId",dayPlan.getPlanProjectId());
                jsonArray.add(jsonObject);
            }
            // (4)将两部分和在一起，然后保存进数据库
            dayPlan.setPlanContent(jsonArray.toJSONString());
            dayPlan = CountNumberUtil.countDayPlan(dayPlan);
            dayPlanDao.update(dayPlan);
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    /**
     * 获取一个计划项目的总结内容
     * @param map
     * @return
     */
    @Override
    public ApiMsg requirePlanProjectSummary(Map<String, Object> map) throws Exception {
        List<Map<String,Object>> list = planProjectDao.select(map);
        Map<String, Object> objectMap = list.get(0);
        String summary = (String)objectMap.get("planProjectSummary");
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(summary);
        return apiMsg;
    }

    /**
     * 获取每日计划的完成情况
     * @param planProjectId
     * @return
     * @throws Exception
     */
    @Override
    public ApiMsg requireDayPlanAllCondition(String planProjectId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("planProjectId",planProjectId);
        List<Map<String, Object>> list = dayPlanDao.select(map);
        List listMidden = new ArrayList();
        for (int i = 0;i < list.size();i++) {
            Map<String, Object> midden = list.get(i);
            DayPlan dayPlan = new DayPlan();
            BeanUtils.populate(dayPlan,midden);
            DayPlanCondition dayPlanCondition = new DayPlanCondition(dayPlan);
            listMidden.add(dayPlanCondition);
        }
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(listMidden);
        return apiMsg;
    }
}
