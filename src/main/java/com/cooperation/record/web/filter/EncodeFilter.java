package com.cooperation.record.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来设置request和response的编码
 * @author cyl
 * @date 2021/10/05
 */
@WebFilter("/*")
public class EncodeFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        // 传入设置utf-8即可
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        // 还需要测试一下
        // 传出要设置text/html
        /*servletResponse.setContentType("text/html;charset=utf-8");*/
        servletResponse.setContentType("application/json;charset=utf-8");
        // 设置一下response返回的数据格式
        // 设置一下请求头
        servletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        servletResponse.setHeader("Access-Control-Allow-Credentials","true");
        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        ((HttpServletResponse)response).addHeader("Access-Control-Max-Age", "3628800");
        ((HttpServletResponse)response).addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        chain.doFilter(request, response);
    }
}