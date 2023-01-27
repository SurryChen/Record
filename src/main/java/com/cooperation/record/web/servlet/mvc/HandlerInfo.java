package com.cooperation.record.web.servlet.mvc;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @description Handler信息。这里的Handler指的是Controller或者其API方法，构成了ControllerInfo的一部分
 */
@Data
public class HandlerInfo {

    /**
     * Controller类名或方法名
     */
    private String handlerName;

    /**
     * Controller类路径，或方法名（形如xxx.xxx.UserController#getUser）
     */
    private String handlerPath;

    /**
     * 访问路径
     */
    private String[] requestPath;
    /**
     * 优先级
     */
    private int priority;
    /**
     * 请求方式
     * @return
     */
    private String[] type;

    /**
     * 如果这个Handler是方法的Handler，那就把这个方法的Method对象存储在这里，可以用于反射调用
     */
    private Method method;

    public HandlerInfo() {
    }

    public HandlerInfo(String handlerName, String handlerPath, String[] requestPath, int priority, String[] type) {
        this.handlerName = handlerName;
        this.handlerPath = handlerPath;
        this.requestPath = requestPath;
        this.priority = priority;
        this.type = type;
    }

    public HandlerInfo(String handlerName, String handlerPath, String[] requestPath, int priority, String[] type, Method method) {
        this.handlerName = handlerName;
        this.handlerPath = handlerPath;
        this.requestPath = requestPath;
        this.priority = priority;
        this.type = type;
        this.method = method;
    }
}
