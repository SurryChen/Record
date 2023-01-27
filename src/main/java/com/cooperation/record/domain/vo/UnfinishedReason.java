package com.cooperation.record.domain.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cooperation.record.domain.pojo.DayPlanGather;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 完成情况统计
 * @author cyl
 * @date 2021/11/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnfinishedReason {

    /**
     * 原因总个数
     */
    private int num = 0;

    /**
     * 时间安排不合理
     * 拖延症
     * 计划内容安排不合理
     * 有突发事件
     * 心情不好不想做
     * 其他
     * 分别使用数字代替这几个原因
     */
    private int one = 0;
    private int two = 0;
    private int three = 0;
    private int four = 0;
    private int five = 0;
    private int six = 0;

    /**
     * 百分比
     */
    private String percentageOne;
    private String percentageTwo;
    private String percentageThree;
    private String percentageFour;
    private String percentageFive;
    private String percentageSix;

    /**
     * 统计数据的方法
     * @param reason
     */
    public void add (String reason) {
        // 将原因转换成JSONArray对象，然后统计
        JSONArray jsonArray = JSON.parseArray(reason);
        // 总数增加
        num = num + jsonArray.size();
        // 开始循环判断是哪一个增加
        for (int i = 0;i < jsonArray.size();i++) {
            String content = jsonArray.getString(i);
            if ("时间安排不合理".equals(content)) {
                one++;
            } else if ("拖延症".equals(content)) {
                two++;
            } else if ("计划内容安排不合理".equals(content)) {
                three++;
            } else if ("有突发事件".equals(content)) {
                four++;
            } else if ("心情不好不想做".equals(content)) {
                five++;
            } else if ("其他".equals(content)) {
                six++;
            }
        }
    }

    public void num () {
        double midden = (double)one/num;
        percentageOne = (int) Math.round(midden * 100) + "%";
        midden = (double)two/num;
        percentageTwo = (int) Math.round(midden * 100) + "%";
        midden = (double)three/num;
        percentageThree = (int) Math.round(midden * 100) + "%";
        midden = (double)four/num;
        percentageFour = (int) Math.round(midden * 100) + "%";
        midden = (double)five/num;
        percentageFive = (int) Math.round(midden * 100) + "%";
        midden = (double)six/num;
        percentageSix = (int) Math.round(midden * 100) + "%";
    }

}
