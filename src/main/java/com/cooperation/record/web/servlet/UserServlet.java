package com.cooperation.record.web.servlet;

import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.domain.pojo.User;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.service.Factory.ServiceProxyFactory;
import com.cooperation.record.service.UserService;
import com.cooperation.record.utils.MailUtil;
import com.cooperation.record.utils.ResponseUtil;
import com.cooperation.record.web.servlet.mvc.annotation.ServletHandler;
import com.cooperation.record.web.servlet.mvc.enums.RequestType;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户的所有操作的Servlet类
 * @author cyl
 * @date 2021/10/11
 */
@ServletHandler("/User")
public class UserServlet extends BaseServlet {

    UserService userService = ServiceProxyFactory.getUserService();
    public static final String CHECK = "check";

    @ServletHandler(value = "/login", type = RequestType.POST)
    protected void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user, map);
            ApiMsg apiMsg = userService.login(user);
            if(apiMsg == null){
                ResponseUtil.send(response,new ApiMsg(ApiInfo.WRONG));
            } else {
                // 登录成功后，将用户信息放入session
                ApiMsg apiMsgOther = userService.getUser(user);
                HttpSession session = request.getSession();
                session.setAttribute("user", apiMsgOther.getData());
                session.setMaxInactiveInterval(60 * 60 * 24 * 30);
                ResponseUtil.send(response, apiMsg);
            }
    }

    @ServletHandler(value = "/register", type = RequestType.POST)
    protected void register(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String check = request.getParameter(CHECK);
        HttpSession session = request.getSession();
        if(check.equals(session.getAttribute(CHECK))){
            System.out.println("验证通过");
            Map<String,String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user,map);
            ResponseUtil.send(response,userService.register(user));
        } else {
            ResponseUtil.send(response,new ApiMsg(ApiInfo.CHECK_WRONG));
        }
    }

    @ServletHandler(value = "/mailModifyPassword", type = RequestType.PUT)
    protected void mailModifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String check = request.getParameter(CHECK);
        System.out.println(check);
        HttpSession session = request.getSession();
        if(check.equals(session.getAttribute(CHECK))){
            System.out.println("验证码正确");
            Map<String,String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user,map);
            ResponseUtil.send(response,userService.mailModifyPassword(user));
        } else {
            ResponseUtil.send(response,new ApiMsg(ApiInfo.CHECK_WRONG));
        }
    }

    @ServletHandler(value = "/getUser", type = RequestType.GET)
    protected void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取邮箱
        /*HttpSession session = request.getSession();
        User userOne = (User) session.getAttribute("user");
        String mailbox = userOne.getMailbox();*/
//        System.out.println("拿到的用户邮箱是"+mailbox);
        String mailbox = "123@qq.com";
        // 根据邮箱查询信息
        User user = new User();
        user.setMailbox(mailbox);
        ApiMsg apiMsg = userService.getUser(user);
        // 异常处理
        if(apiMsg == null){
            apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
        // 返回数据
        ResponseUtil.send(response,apiMsg);
    }

    @ServletHandler(value = "/updateUser", type = RequestType.PUT)
    protected void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 获取要更新的字段，分两种情况，一种是密码，一种密码以外的信息更改
            String update = request.getParameter("update");
            // 使用map集合保存信息，不能从request中获取map
            Map<String, Object> map = new HashMap<>();
            if (update.equals("password")) {
                map.put("before", request.getParameter("before"));
            }
            // 保存id
            map.put("userId", request.getParameter("userId"));
            map.put(update, request.getParameter(update));
            ApiMsg apiMsg = userService.updateUser(map);
            if (apiMsg == null) {
                apiMsg = new ApiMsg(ApiInfo.WRONG);
                ResponseUtil.send(response,apiMsg);
            } else {
                if(apiMsg.getCode() == 1002){
                    ResponseUtil.send(response,apiMsg);
                } else {
                    request.getRequestDispatcher("/User/getUser").forward(request,response);
                }
            }
        } catch (Exception e){
            ResponseUtil.send(response,new ApiMsg(ApiInfo.WRONG));
        }
    }

    @ServletHandler(value = "/mailCheck", type = RequestType.POST)
    protected void mailCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String mail = request.getParameter("mailbox");
            // 生成六位的随机数
            StringBuilder check = new StringBuilder();
            for(int i = 0;i < 6;i++){
               check.append("" + (int) (Math.random() * 10));
            }
            MailUtil.sendMail(mail, "验证码是" + check, "呱呱");
            HttpSession session = request.getSession();
            session.setAttribute("check", check.toString());
            ResponseUtil.send(response,new ApiMsg(ApiInfo.SUCCESS));
        } catch (Exception e){
            ResponseUtil.send(response,new ApiMsg(ApiInfo.WRONG));
        }
    }

}