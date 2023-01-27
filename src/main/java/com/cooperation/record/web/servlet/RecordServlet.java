package com.cooperation.record.web.servlet;

import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.domain.pojo.FileFolder;
import com.cooperation.record.domain.pojo.Record;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.service.Factory.ServiceProxyFactory;
import com.cooperation.record.service.RecordService;
import com.cooperation.record.utils.ResponseUtil;
import com.cooperation.record.utils.TranslateUtil;
import com.cooperation.record.web.servlet.mvc.annotation.ServletHandler;
import com.cooperation.record.web.servlet.mvc.enums.RequestType;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与record页面的相关操作
 * @author cyl
 * @date 2021/10/16
 */
@ServletHandler("/Record")
public class RecordServlet extends BaseServlet {

    RecordService recordService = ServiceProxyFactory.getRecordService();

    @ServletHandler(value = "/saveRecord", type = RequestType.PUT)
    public void saveRecord(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,String[]> map = request.getParameterMap();
        String novel = request.getParameter("novel");
        Record record = new Record();
        BeanUtils.populate(record,map);
        // 把初始化的对象传给service层中的类
        ApiMsg msg = recordService.saveRecord(record);
        if(msg == null){
            msg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,msg);
    }

    @ServletHandler(value = "/requireRecord", type = RequestType.GET)
    public void requireRecord(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("userId",request.getParameter("userId"));
        map.put("page",request.getParameter("page"));
        map.put("catalogue",request.getParameter("catalogue"));
        ApiMsg<List<Record>> apiMsg = recordService.requireRecord(map);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    @ServletHandler(value = "/requireRecordAll", type = RequestType.GET)
    public void requireRecordAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("userId",request.getParameter("userId"));
        map.put("catalogue",request.getParameter("catalogue"));
        ApiMsg<List<Record>> apiMsg = recordService.requireRecordAll(map);
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        System.out.println("获取记录");
        ResponseUtil.send(response,apiMsg);
    }

    @ServletHandler(value = "/searchRecord", type = RequestType.POST)
    public void searchRecord(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("userId",request.getParameter("userId"));
        map.put("search",request.getParameter("search"));
        map.put("page",request.getParameter("page"));
        ApiMsg<List<Record>> msg = recordService.searchRecord(map);
        if(msg == null){
            msg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,msg);
    }

    @ServletHandler(value = "/deleteRecord", type = RequestType.DELETE)
    protected void deleteRecord(HttpServletRequest request,HttpServletResponse response) throws Exception{
        try {
            Map<String,String[]> map = request.getParameterMap();
            Record record = new Record();
            BeanUtils.populate(record,map);
            ResponseUtil.send(response,recordService.deleteRecord(record));
            System.out.println("删除记录");
        } catch (Exception e){
            ResponseUtil.send(response,new ApiMsg(ApiInfo.WRONG));
        }
    }

    @ServletHandler(value = "/deleteRecord", type = RequestType.POST)
    protected void translate(HttpServletRequest request,HttpServletResponse response) throws Exception{
        try {
            String translate = request.getParameter("translate");
            translate = TranslateUtil.parseJSONWithJSONObject(translate);
            ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
            apiMsg.setData(translate);
            ResponseUtil.send(response,apiMsg);
        } catch (Exception e){
            e.printStackTrace();
            ResponseUtil.send(response,new ApiMsg(ApiInfo.WRONG));
        }
    }

    @ServletHandler(value = "/fileFolder", type = RequestType.GET)
    protected void fileFolder(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String, String[]> map = request.getParameterMap();
        FileFolder fileFolder = new FileFolder();
        BeanUtils.populate(fileFolder,map);
        String update = request.getParameter("update");
        // “operation”:”0”（0就是创建，1就是删除，2就是修改）
        String operation = request.getParameter("operation");
        ApiMsg apiMsg = recordService.fileFolder(fileFolder,operation,update);
        if(apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

    @ServletHandler(value = "/requireFileTree", type = RequestType.GET)
    protected void requireFileTree(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String, String[]> map = request.getParameterMap();
        FileFolder fileFolder = new FileFolder();
        BeanUtils.populate(fileFolder,map);
        ApiMsg apiMsg = recordService.requireFileTree(fileFolder);
        if(apiMsg == null) {
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        ResponseUtil.send(response,apiMsg);
    }

}
