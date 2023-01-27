package com.cooperation.record.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一篇记录一个对象
 * @author cyl
 * @date 2021/10/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    private int recordId;
    private String catalogue;
    private int userId;
    private String title;
    private String novel;
    private String novelTxt;
    private String createTime;
    private String updateTime;
    private String overheadTime;
}
