package com.cooperation.record.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理工具类
 * @author cyl
 * @date 2021/10/22
 */
public class CalendarUtil {

    /**
     * 获取今天到几天后的每一天的日期
     * 如果是后几天的话
     * calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + i);
     * 改成“-”就可以了
     * @param cal
     * @param day 多少天后
     * @return 返回一个日期的字符数组以供插入
     */
    public static String[] getDateAfter(String cal, int day) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 字符串转date
        Date date = format.parse(cal);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] dates = new String[day+1];
        try {
            for (int i = 0; i <= day ; i++) {
                // +1会是calendar里面的日历更新加1，是可以累加的
                // calendar.get(1)是年份，calendar.get(5)是日期，Calendar是一个枚举
                // ser(int,int)，前者说明要替换的位置，后者是替换的数值
                // 替换的数值一旦超过calendar当月的天数，会自动变回1
                dates[i] = format.format(calendar.getTime());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dates;
    }

    /**
     * 获取两个日期之间相隔的天数
     * 通过获取毫秒之间的差值来获取之间的天数差
     * @param
     */
    public static int getTwoDays(String createTime, String endTime) throws ParseException {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        Date one = myFormatter.parse(createTime);
        Date two = myFormatter.parse(endTime);
        day = (int)((two.getTime() - one.getTime()) / (24 * 60 * 60 * 1000));
        return day;
    }

    /**
     * 获取这周周一的日期
     */
    public static String getThisMonDate() throws ParseException {
        // 获取今天是周几
        int weekday = getDayOfWeek();
        if(weekday == 1) {
            weekday = 7;
        } else {
            weekday--;
        }
        // 获取一个日历
        Calendar calendar = Calendar.getInstance();
        // 设置日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当天日期前几天的日期，也就是周一的日期
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - weekday + 1);
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }


    /**
     * 获取这周周日的日期
     */
    public static String getThisSunDate() throws ParseException {
        // 获取今天是周几
        int weekday = getDayOfWeek();
        if(weekday == 1) {
            weekday = 7;
        } else {
            weekday--;
        }
        // 获取一个日历
        Calendar calendar = Calendar.getInstance();
        // 设置日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当天日期前几天的日期，也就是周日的日期
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7 -weekday);
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    /**
     * 获取下一周周一的日期
     */
    public static String getNextMonDate() throws ParseException {
        // 获取今天是周几
        int weekday = getDayOfWeek();
        if(weekday == 1) {
            weekday = 7;
        } else {
            weekday--;
        }
        // 获取一个日历
        Calendar calendar = Calendar.getInstance();
        // 设置日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当天日期前几天的日期，也就是周一的日期
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - weekday + 1 + 7);
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    /**
     * 获取下一周周日的日期
     */
    public static String getNextSunDate() throws ParseException {
        // 获取今天是周几
        int weekday = getDayOfWeek();
        if(weekday == 1) {
            weekday = 7;
        } else {
            weekday--;
        }
        // 获取一个日历
        Calendar calendar = Calendar.getInstance();
        // 设置日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当天日期前几天的日期，也就是周日的日期
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7 -weekday + 7);
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    /**
     * 获取当天是周几的日期
     * 从周日开始计算，周日是1
     * @return
     * @throws ParseException
     */
    public static int getDayOfWeek() throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
