package com.cooperation.record.web.servlet.mvc;

import com.alibaba.fastjson.JSONObject;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.utils.GetParamUtil;
import com.cooperation.record.utils.ResponseUtil;
import com.cooperation.record.web.servlet.mvc.enums.RequestType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @description DispatcherController接管所有请求，并寻找合适的Controller方法进行调用
 */
@WebServlet("/")
public class DispatcherController extends HttpServlet {

    final String HTML = ".html";
    final String JSP = ".jsp";

    /**
     * 所有Controller的API信息
     */
    private List<ControllerInfo<?>> controllerInfos;

    public DispatcherController() {
        List<ControllerInfo<?>> infoList = ControllerScanner.doScan();
        // 不可修改的集合
        this.controllerInfos = Collections.unmodifiableList(infoList);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DispatcherController run!");
        doDispatch(req, resp);
    }

    /**
     * 派发请求
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 解析req的请求地址，即localhost:8080/之后的路径
        String uri = req.getRequestURI();
        System.out.println(uri);
        try {
            // 如果请求包含了html，说明是请求页面，直接交予原生Servlet处理
            if (uri.contains(HTML) || uri.contains(JSP)) {
                super.service(req, resp);
                return;
            }
            // HTTP请求类型
            String reqMethodType = req.getMethod();
            // 获取Controller的信息
            ControllerInfo<?> controllerInfo = getHandleControllerInfo(reqMethodType, uri);
            // 如果获取不到，就是用原生Servlet响应请求
            if (controllerInfo == null) {
                super.service(req, resp);
                return;
            }
            // 获取Controller的API方法信息
            Method method = getHandleMethod(reqMethodType, uri, controllerInfo);
            // 如果获取不到，就是用原生Servlet响应请求
            if (method == null) {
                super.service(req, resp);
                return;
            }
            // 获取Controller实例，用于反射
            Object bean = BeanFactory.getBean(controllerInfo.getBeanName());
            // 反射调用Controller的API方法
            Object msg;
            try {
                msg = doInvoke(req, resp, bean, method);
            } catch (UnsupportedOperationException e) {
                System.out.println("ContentType不支持，使用原生的Servlet响应请求");
                super.service(req, resp);
                return;
            }
            // 向前端返回响应结果
            ResponseUtil.send(resp, JSONObject.toJSONString(msg));

        } catch (IllegalArgumentException e) {
            System.err.println("反射的方法参数不匹配：" + e.getMessage());
            resp.setStatus(500);
            ResponseUtil.send(resp, new ApiMsg("反射的方法参数不匹配：" + e.getMessage()));
        } catch (InvocationTargetException e) {
            // 反射方法执行时出现异常，获取原始异常，输出报错信息
            e.getCause().printStackTrace();
            resp.setStatus(500);
            // 获取原始异常
            ResponseUtil.send(resp, new ApiMsg("后端执行异常：" + e.getCause().getMessage()));
        } catch (Exception e) {
            // 未知异常
            e.printStackTrace();
            resp.setStatus(500);
            ResponseUtil.send(resp, new ApiMsg(e.getMessage()));
        }
    }

    /**
     * 获取请求的Controller对象的API信息
     * @param reqMethod
     * @param uri
     * @return
     */
    private ControllerInfo<?> getHandleControllerInfo(String reqMethod, String uri) {
        // 遍历所有Controller
        for (ControllerInfo<?> controllerInfo : controllerInfos) {
            HandlerInfo controllerHandlerInfo = controllerInfo.getControllerHandlerInfo();
            // 判断是否请求了该Controller
            for (String requestControllerPath : controllerHandlerInfo.getRequestPath()) {
                if (uri.contains(requestControllerPath)) {
                    return controllerInfo;
                }
            }
        }
        return null;
    }

    /**
     * 获取请求的Controller方法对象
     * @param reqMethod
     * @param uri
     * @return
     */
    private Method getHandleMethod(String reqMethod, String uri, ControllerInfo<?> controllerInfo) {
        List<HandlerInfo> methodHandlerInfos = controllerInfo.getMethodHandlerInfos();
        // 遍历该Controller的所有API方法
        for (HandlerInfo methodHandlerInfo : methodHandlerInfos) {
            // 判断是否请求了该方法
            for (String requestMethodPath : methodHandlerInfo.getRequestPath()) {
                if (uri.contains(requestMethodPath)) {
                    // 判断HTTP请求类型是否一致
                    String[] httpMethodTypes = methodHandlerInfo.getType();
                    for (String methodType : httpMethodTypes) {
                        if (reqMethod.equals(methodType)) {
                            return methodHandlerInfo.getMethod();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 反射调用方法
     */
    private Object doInvoke(HttpServletRequest req, HttpServletResponse resp, Object instance, Method method) throws InvocationTargetException, IllegalAccessException {
        // 获取请求参数
        String reqMethod = req.getMethod();
        JSONObject params = null;
        if (RequestType.GET.equals(reqMethod)) {
            // GET请求从URL中获取参数
            params = GetParamUtil.getJsonByUrl(req);
        } else {
            // 其他请求，先判断是不是用json传参
            String contentType = req.getContentType();
            if ("application/json".equals(contentType)) {
                // 参数通过json提交
                params = GetParamUtil.getJsonByJson(req);
            } else if ("application/x-www-form-urlencoded".equals(contentType)) {
                // form表单提交
                params = GetParamUtil.getJsonByForm(req);
            } else {
                // 文件上传请求 multipart/form-data
                // 这种提交方式提交特殊，正常方式没办法获取参数，待完善。
                // 这里抛出异常后使用原生的Servlet响应请求
//                throw new UnsupportedOperationException("暂不支持该请求方式");
            }
        }
        // 反射调用Controller方法，请求参数固定
        return method.invoke(instance, req, resp, params);
    }
}
