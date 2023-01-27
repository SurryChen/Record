package com.cooperation.record.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cooperation.record.domain.pojo.DayPlan;
import com.cooperation.record.domain.pojo.PlanProject;

/**
 * 传入pojo，统计数据
 * @author cyl
 * @date 2021/11/18
 */
public class CountNumberUtil {

    /**
     * 这个是统计计划项目里面的数据
     */
    public static PlanProject countPlanProject (PlanProject planProject) {
        // 拿到目标内容，并转为JSONArray对象
        String json = planProject.getPlanProjectGoal();
        JSONArray jsonArray = JSONArray.parseArray(json);
        // 统计完成与未完成
        int planFinished = 0;
        int planUnfinished = 0;
        for(int i = 0;i < jsonArray.size();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String condition = (String)jsonObject.get("condition");
            if("是".equals(condition)) {
                planFinished++;
            } else if ("否".equals(condition)) {
                planUnfinished++;
            }
        }
        planProject.setPlanFinished(planFinished);
        planProject.setPlanUnfinished(planUnfinished);
        return planProject;
    }

    /**
     * 写一个统计日计划中的事项完成情况
     */
    public static DayPlan countDayPlan (DayPlan dayPlan) {
        try {
            // 拿到目标内容，并转为JSONArray对象
            String json = dayPlan.getPlanContent();
            System.out.println("-----------------");
            System.out.println(json);
            System.out.println("------------------");
            JSONArray jsonArray = JSONArray.parseArray(json);
            // 统计完成与未完成
            int planFinished = 0;
            int planUnfinished = 0;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String condition = (String) jsonObject.get("condition");
                System.out.println("---------------------");
                System.out.println("完成情况"+condition);
                System.out.println("---------------------");
                if ("是".equals(condition)) {
                    planFinished++;
                } else if ("否".equals(condition)) {
                    planUnfinished++;
                }
            }
            System.out.println("个数："+planFinished);
            System.out.println("个数："+planUnfinished);
            dayPlan.setPlanFinished(planFinished);
            dayPlan.setPlanUnfinished(planUnfinished);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return dayPlan;
    }
}
