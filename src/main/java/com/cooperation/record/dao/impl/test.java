package com.cooperation.record.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cooperation.record.utils.CalendarUtil;

import java.util.Calendar;

public class test {

    public static void main(String[] args) throws Exception {
//        RecordServiceImpl recordService = new RecordServiceImpl();
//        recordService.deleteFileFolder(4,"/a");
//        String novel = "[\"没吃饱\",\"没吃好\"]";
//        JSONArray jsonArray = JSON.parseArray(novel);
//        String a = jsonArray.getString(0);
//        String b = jsonArray.getString(1);
//        System.out.println(a + "-------" + b);
//        System.out.println(jsonArray.get(0));
//        System.out.println(jsonArray.get(1));
        String date = "2021-11-01";
        String two = "2021-11-07";
        int i = CalendarUtil.getTwoDays(date,two);
        String[] a = CalendarUtil.getDateAfter(date,i);
        for(int b = 0;b < a.length;b++) {
            System.out.println(a[b]);
        }
    }

}
