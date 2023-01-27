package com.cooperation.record.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author cyl
 * @date 2021/10/05
 */
public class LoginCheckFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        if(session.getAttribute("user") != null) {
            chain.doFilter(request,response);
        } else {
            ((HttpServletResponse)response).sendRedirect("http://localhost:8080/register/regist.html");
        }
    }
}
