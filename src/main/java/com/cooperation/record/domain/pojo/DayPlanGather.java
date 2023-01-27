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
public class DayPlanGather {
    private int userId;
    private String planDate;
    private String planSummary;
    private String reason;
}