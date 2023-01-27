package com.cooperation.record.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 运行js
 */
public class RequireTimeTable {

    public static JSONObject midden(String account, String password, String term, int week) throws Exception {

        String session = getPostMethod(account, password);
        if (session.length() == 0) {
            return null;
        }
        String path = tablePath(session, term, week);
        // 读取一下文件
        JSONObject jsonObject = tableFile(path);
        return jsonObject;

    }

    public static JSONObject tableFile(String path) throws IOException {

        try {
            // 创建文件输入流
            FileInputStream fileInput = null;
            /**
             * .xls文件手动修改后缀为.xlsx会报错
             */
            /**
             * 有两种工作簿
             * 格式一旦不对就会报错
             * 抓一下异常，报错就换一种格式
             */
            Workbook workbook = null;
            try {
                fileInput = new FileInputStream(path);
                workbook = new XSSFWorkbook(fileInput);
            }catch (Exception e){
                fileInput = new FileInputStream(path);
                workbook = new HSSFWorkbook(fileInput);
            }
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 同样应该读取列而不是行
            /**
             * 因为设置的时候就是按照列来编辑进入JSON格式数据
             * 所以要分列
             */
            // 获取表格内容的最后一行的行数
            int lastRowNum = sheet.getLastRowNum();
            // 获取最大列数
            Row rowOther = sheet.getRow(0);
            int col = rowOther.getLastCellNum();
            // 不是有七天的不行
            if (col != 8) {
                return null;
            }
            // 标记一下第三行，第三行也就是有星期几的一行
            Row rowThree = sheet.getRow(2);
            // 返回的最外层的JSON格式数据
            JSONObject jsonObject = new JSONObject();
            // 使用列遍历
            // 从Excel中的第四行开始，也就是这里的3
            for (int i = 1; i < col; i++) {
                // 每一天的键值
                List<JSONObject> listJson = new ArrayList<>();
                // 使用行遍历
                for (int h = 3; h < lastRowNum; h++) {
                    JSONObject jsonObjectOne = new JSONObject();
                    Row rowOne = sheet.getRow(h);
                    Cell cell = rowOne.getCell(i);
                    // 判断单元格是否为空
                    if(isEmptyCell(cell) || cell.getStringCellValue().equals(" ")){
                        continue;
                    }
                    // 拿到内容
                    String content = cell.getStringCellValue();
                    String[] novel = content.split("\n");
                    String middenContent = novel[1];
                    /**
                     * 加入数据
                     */
                    jsonObjectOne.put("planProjectId",0);
                    // 加入的时间，主要看行数
                    String startTime = null;
                    String endTime = null;
                    // 根据行数设置时间
                    if(h == 3) {
                        startTime = "08:20";
                        endTime = "09:55";
                    } else if (h == 4) {
                        startTime = "10:15";
                        endTime = "11:50";
                    } else if (h == 6) {
                        startTime = "14:00";
                        endTime = "15:35";
                    } else if (h == 7) {
                        startTime = "15:40";
                        endTime = "16:25";
                    } else if (h == 8) {
                        startTime = "16:35";
                        endTime = "18:10";
                    } else if (h == 9) {
                        startTime = "19:00";
                        endTime = "20:35";
                    }
                    jsonObjectOne.put("startTime",startTime);
                    jsonObjectOne.put("endTime",endTime);
                    // 不能直接加入单元格啊
                    jsonObjectOne.put("content",middenContent);
                    jsonObjectOne.put("condition","否");
                    listJson.add(jsonObjectOne);
                }
                jsonObject.put(rowThree.getCell(i).getStringCellValue(),listJson);
            }
            workbook.close();
            fileInput.close();
            return jsonObject;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断单个单元格是否为空
     */
    public static boolean isEmptyCell(Cell cell) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }
        return false;
    }

    public static String getPostMethod(String account,String password) throws Exception {
        //1.打开浏览器，创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2.输入网址，发起请求创建HttpPost对象，设置url访问地址
        HttpPost httpPost = new HttpPost("http://jwxt.gduf.edu.cn/jsxsd/xk/LoginToXk");


        //声明List集合，封装表单中的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //设置请求地址是://
        params.add(new BasicNameValuePair("encoded", key(account,password)));


        //创建表单的Entity对象，第一个参数就是封装好的表单数据，第二个参数就是编码
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");

        //设置表单的Entity对象到Post请求中

        httpPost.setEntity(formEntity);


        CloseableHttpResponse response = httpClient.execute(httpPost);
        //4.解析响应，获取数据

        //判断状态码是否为200
        String session=null;
        if (response.getStatusLine().getStatusCode() == 302) {
//            HttpEntity httpEntity = response.getEntity();
//            String content = EntityUtils.toString(httpEntity, "utf8");
            Header header = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD 中的
            String newuri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
            Header[] headers = response.getHeaders("Set-Cookie");
            String headerstr = headers.toString();
            String cookie = "";
            for (int i = 0; i < headers.length; i++) {//取出所有的cookie值
                String value = headers[i].getValue();
                cookie += value;
            }
            String[] sa=cookie.split(";");
            session=sa[0];
        }
        httpClient.close();
        return session;
    }

    public static String get(String session) throws IOException {
        String cookie = session;
        URL url = new URL("http://jwxt.gduf.edu.cn/jsxsd/framework/xsMain.jsp");
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Cookie", cookie);
        conn.setDoInput(true);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
//            System.out.println("请求响应结果："+sb);
//            DaoTest.html2Text(sb.toString());
//            System.out.println(DaoTest.html2Text(sb.toString()));
//            System.out.println(sb.toString().lastIndexOf("姓名"));
        int x1=sb.toString().lastIndexOf("姓名");
//            System.out.println(sb.toString().substring(9207,9214));

        int i = x1+3;
        StringBuilder stringBuilder=new StringBuilder();
        while(sb.charAt(i)!='<'){
            stringBuilder.append(sb.charAt(i));
            i++;
        }
        return stringBuilder.toString();
    }

    public static String key(String name, String password) {

        String nameOther = null;
        String passwordOther = null;

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.eval("var keyStr = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\";\n" +
                    "\n" +
                    "    function encodeInp(input) {\n" +
                    "        input = input + \"\";\n" +
                    "        var output = \"\";\n" +
                    "        var chr1, chr2, chr3 = \"\";\n" +
                    "        var enc1, enc2, enc3, enc4 = \"\";\n" +
                    "        var i = 0;\n" +
                    "        do {\n" +
                    "            chr1 = input.charCodeAt(i++);\n" +
                    "            chr2 = input.charCodeAt(i++);\n" +
                    "            chr3 = input.charCodeAt(i++);\n" +
                    "            enc1 = chr1 >> 2;\n" +
                    "            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);\n" +
                    "            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);\n" +
                    "            enc4 = chr3 & 63;\n" +
                    "            if (isNaN(chr2)) {\n" +
                    "                enc3 = enc4 = 64\n" +
                    "            } else if (isNaN(chr3)) {\n" +
                    "                enc4 = 64\n" +
                    "            }\n" +
                    "            output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);\n" +
                    "            chr1 = chr2 = chr3 = \"\";\n" +
                    "            enc1 = enc2 = enc3 = enc4 = \"\"\n" +
                    "        } while (i < input.length);\n" +
                    "        return output;\n" +
                    "    }");
            if (engine instanceof Invocable) {
                Invocable in = (Invocable) engine;
                nameOther = in.invokeFunction("encodeInp", name) + "";
                passwordOther = in.invokeFunction("encodeInp", password) + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameOther + "%%%" + passwordOther;
    }

    public static String tablePath (String ip, String term, int week) {
        try{
            // xnxq01id为学期号，zc为周数
            String path = "http://jwxt.gduf.edu.cn/jsxsd/xskb/xskb_print.do?xnxq01id="+term+"&zc="+week;
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            con.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
            con.addRequestProperty("Cookie", ip);

            con.connect();
            if (con.getResponseCode() == 200) {

                InputStream inputStream = con.getInputStream();
                File file = new File("D:\\Record\\src\\main\\webapp\\table\\b.xlsx");
                OutputStream out = new FileOutputStream(file);
                int size = 0;
                int len = 0;
                byte[] buf = new byte[2048];
                while ((size = inputStream.read(buf)) != -1) {
                    len += size;
                    out.write(buf, 0, size);
                    // 控制台打印文件下载的百分比情况
                }
                // 关闭资源
                inputStream.close();
                out.close();

            }else{
                System.out.println(con.getResponseCode());
            }
            con.disconnect();// 断开连接
        }catch (Exception e){

            System.out.println(e);
        }
        return "D:\\Record\\src\\main\\webapp\\table\\b.xlsx";
    }
}
