package com.cooperation.record.domain.vo;

import com.cooperation.record.domain.pojo.DayPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在前后端传输信息的javaBean对象
 * @author cyl
 * @date 2021/11/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayPlanCondition {

    /**
     * 完成个数
     */
    private int planFinished;
    /**
     * 未完成个数
     */
    private int planUnfinished;
    /**
     * 日期
     */
    private String date;
    /**
     * 完成占比
     */
    private String percentage;
    /**
     * 是否全部完成
     */
    private boolean finishAll;

    public DayPlanCondition (DayPlan dayPlan) {
        this.planFinished = dayPlan.getPlanFinished();
        this.planUnfinished = dayPlan.getPlanUnfinished();
        date = dayPlan.getPlanDate();
        double midden = (double)planFinished/(planUnfinished + planFinished);
        percentage = (int) Math.round(midden * 100) + "%";
        if("100%".equals(percentage)) {
            finishAll = true;
        } else {
            finishAll = false;
        }
    }
}
