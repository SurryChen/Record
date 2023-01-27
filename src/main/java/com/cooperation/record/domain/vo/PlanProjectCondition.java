package com.cooperation.record.domain.vo;

import com.cooperation.record.domain.pojo.PlanProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计划的完成情况
 * @author cyl
 * @date 2021/11/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanProjectCondition {

    private int planFinished;
    private int planUnfinished;
    private int planAll;

    public PlanProjectCondition (PlanProject planProject) {
        this.planFinished = planProject.getPlanFinished();
        this.planUnfinished = planProject.getPlanUnfinished();
        planAll = planProject.getPlanFinished() + planProject.getPlanUnfinished();
    }

}
