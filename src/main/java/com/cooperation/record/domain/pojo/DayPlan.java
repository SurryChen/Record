package com.cooperation.record.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cyl
 * @date 2021/10/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayPlan {
    private int planProjectId;
    private String planProjectType;
    private String planDate;
    private String planContent;
    private int planFinished;
    private int planUnfinished;
}
