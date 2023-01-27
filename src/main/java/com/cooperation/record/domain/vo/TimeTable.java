package com.cooperation.record.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 所要获取的课表的信息
 * @author cyl
 * @date 2021/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable {

    /**
     * 用户id
     */
    private int userId;
    /**
     * 学期
     */
    private String term;
    /**
     * 周数
     */
    private int week;
    /**
     * 本周还是下一周
     */
    private int when;


}
