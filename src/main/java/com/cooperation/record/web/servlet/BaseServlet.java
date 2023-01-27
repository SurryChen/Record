package com.cooperation.record.web.servlet;

import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.utils.ResponseUtil;
import lombok.SneakyThrows;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 所有Servlet的父类
 * @author cyl
 * @date 2021/10/11
 */
public class BaseServlet extends HttpServlet {

    @SneakyThrows
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取方法名
        String url = request.getRequestURI();
        // 找到最后一个/的位置，并加1取后面所有
        String methodName = url.substring(url.lastIndexOf('/') + 1);
        // 开始加载这个类的对应方法进入内存
        Method[] methods = this.getClass().getDeclaredMethods();
        Method method = this.getClass().getDeclaredMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
        // 使用invoke执行这个方法
        try {
            method.invoke(this, request, response);
        } catch (Exception e){
            ResponseUtil.send(response,new ApiMsg(ApiInfo.WRONG));
        }
    }

}
