package com.cooperation.record.utils;

import com.alibaba.fastjson.JSONObject;
import com.cooperation.record.domain.vo.ApiMsg;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 后端返回信息到前端的工具类
 * @author cyl
 * @date 2021/10/11
 */
public class ResponseUtil {
    public static void send(HttpServletResponse response, ApiMsg msg) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg",msg);
        response.setContentType("application/json");
        response.getWriter().write(jsonObject.toJSONString());
    }

    public static void send(HttpServletResponse response, String msg) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg",msg);
        response.setContentType("application/json");
        response.getWriter().write(jsonObject.toJSONString());
    }
}
