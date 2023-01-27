package com.cooperation.record.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author wtk
 * @description 从HttpServletRequest中获取参数
 */
public class GetParamUtil {
    /**
     * 获取json参数，保存在JSONObject对象中
     * @param req
     * @return
     */
    public static JSONObject getJsonByJson(HttpServletRequest req) {
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(
                    req.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            // 将所有字段连接一个字符串，然后再转json对象
            JSONObject resultJson = JSONObject.parseObject(responseStrBuilder.toString());
            return resultJson;
        } catch (Exception e) {
            System.err.println("解析json 失败 " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过url获取参数，存到json中
     * @param req
     * @return
     */
    public static JSONObject getJsonByUrl(HttpServletRequest req) {
        try {
            String urlParams = req.getQueryString();
            if ("".equals(urlParams) || urlParams == null) {
                System.err.println("getJsonByUrl 无参数");
                return null;
            }
            //切割 & ，得到一个个键值对
            String[] params = urlParams.split("&");
            JSONObject json = new JSONObject();
            for (String p : params) {
                //切割键值对，存到json中
                String[] kv = p.split("=");
                json.put(kv[0], kv[1]);
            }
            return json;
        } catch (Exception e) {
            System.err.println("解析json 失败 " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过form表单获取参数，封装为对象。可能会遗漏数据
     * @param req
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObjByForm(HttpServletRequest req, Class<T> clazz) {
        try {
            JSONObject json = new JSONObject();
            for (Field field : clazz.getDeclaredFields()) {
                String key = field.getName();
                String parameter = req.getParameter(key);
                json.put(key, parameter);
            }
            return json.toJavaObject(clazz);
        } catch (Exception ex) {
            System.err.println("解析 formData 失败。Message：" + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 通过表单获取数据，保存在json中
     * @param req
     * @return
     */
    public static JSONObject getJsonByForm(HttpServletRequest req) {
        JSONObject json = new JSONObject();
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            json.put(entry.getKey(), entry.getValue()[0]);
        }
        return json;
    }
}
