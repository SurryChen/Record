package com.cooperation.record.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.utils.FileUtil;
import com.cooperation.record.utils.PictureUtil;
import com.cooperation.record.utils.ResponseUtil;
import com.cooperation.record.utils.WordToHtmlBackStringUtil;
import com.cooperation.record.web.servlet.mvc.annotation.ServletHandler;
import com.cooperation.record.web.servlet.mvc.enums.RequestType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取文件并放回路径
 *
 * @author cyl
 * @date 2021/10/27
 */
@ServletHandler("/File")
public class FileServlet {

    /**
     * 图文排版处文件上传
     */
    @ServletHandler(value = "/FilepictureFileUpload", type = RequestType.POST)
    public void pictureFileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String real = "D:\\Record\\src\\main\\webapp\\picture";
        String fileType = "picture";
        ApiMsg apiMsg = FileUtil.load(request, real, fileType);
        if (apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response, apiMsg);
    }

    /**
     * xlsx、xls文件上传
     */
    @ServletHandler(value = "/tableFileUpload", type = RequestType.POST)
    public void tableFileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String real = "D:\\Record\\src\\main\\webapp\\table";
        String fileType = "table";
        ApiMsg<JSONObject> apiMsg = FileUtil.load(request, real, fileType);
        System.out.println("servlet中" + apiMsg.getData());
        if (apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        } else {
            // 上传成功了，但是文件格式还没有判断
        }
        ResponseUtil.send(response, apiMsg);
    }

    /**
     * 下载图片
     */
    @ServletHandler(value = "/pictureLoad", type = RequestType.GET)
    public void pictureLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String content = request.getParameter("content");
        String title = request.getParameter("title");
        response.setContentType("image/jpeg");
        String path = PictureUtil.picture(title, content);
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(path.substring(34));
        ResponseUtil.send(response, apiMsg);
    }


    /**
     * 获取名字拿到图片
     */
    @ServletHandler(value = "/requirePicture", type = RequestType.GET)
    public void requirePicture(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String filename = request.getParameter("filename");

        response.addHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);

        FileInputStream in = new FileInputStream("D:\\Record\\src\\main\\webapp\\picture\\" + filename);
        OutputStream out = response.getOutputStream();

        try {
            byte[] buffer = new byte[in.available()];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
        }

        in.close();
        out.close();

    }

    /**
     * excel表格模板
     */
    @ServletHandler(value = "/excelLoad", type = RequestType.GET)
    public void excelLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType("application/vnd.ms-excel");

        String path = "D:\\Record\\src\\main\\webapp\\table\\模板.xlsx";

        String filename = "example.xlsx";

        response.addHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);

        FileInputStream in = new FileInputStream(path);
        OutputStream out = response.getOutputStream();

        try {
            byte[] buffer = new byte[in.available()];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
        }
        in.close();
        out.close();

    }

    /**
     * 上传word文件，返回HTML标签
     */
    @ServletHandler(value = "/wordToHtml", type = RequestType.POST)
    public void wordToHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String real = "D:\\Record\\src\\main\\webapp\\word";
        String fileType = "word";
        ApiMsg apiMsg = FileUtil.load(request, real, fileType);
        List<String> stringList = new ArrayList<>();
        try {
            if (apiMsg == null) {
                ResponseUtil.send(response, new ApiMsg(ApiInfo.WRONG));
            } else if (apiMsg.getCode() == 1008) {
                List<String> list = (List) apiMsg.getData();
                for (int i = 0; i < list.size(); i++) {
                    String path = list.get(i);
                    String html = WordToHtmlBackStringUtil.convert(path);
                    stringList.add(html);
                }
                apiMsg.setData(stringList);
                ResponseUtil.send(response, apiMsg);
            } else {
                ResponseUtil.send(response, apiMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
