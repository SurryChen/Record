package com.cooperation.record.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计划项目的实体类
 * @author cyl
 * @date 2021/10/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanProject {
    private int planProjectId;
    private String planProjectType;
    private int userId;
    private String planProjectName;
    private String planProjectDateStart;
    private String planProjectDateEnd;
    private String planProjectCreateTime;
    private String planProjectUpdateTime;
    private String planProjectGoal;
    private String planProjectSummary;
    private int planFinished;
    private int planUnfinished;
}
