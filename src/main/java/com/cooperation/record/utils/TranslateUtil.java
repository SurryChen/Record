package com.cooperation.record.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;

/**
 * 翻译工具类
 * @author cyl
 * @date 2021/11/01
 */
public class TranslateUtil {

    /**
     * 用 JSONObject 解析
     * @param translate
     */
    public static String parseJSONWithJSONObject(String translate) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i="+translate)
                .method("POST",body)
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        String replaced = response.body().string();
        System.out.println(replaced);
        JSONObject jsonObject = JSONObject.parseObject(replaced);
        System.out.println(jsonObject);
        String jsonReturn = jsonObject.get("translateResult").toString();
        return jsonReturn.substring(1,jsonReturn.length()-1);
    }

}
