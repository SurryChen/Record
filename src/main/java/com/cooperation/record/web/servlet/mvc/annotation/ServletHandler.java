package com.cooperation.record.web.servlet.mvc.annotation;

import com.cooperation.record.web.servlet.mvc.enums.RequestType;

import java.lang.annotation.*;

/**
 * @description 用于表示Controller类和方法处理的请求的注解。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServletHandler {

    /**
     * 等价于urlPatterns注解
     */
    String[] value();

    /**
     * Controller或其方法所处理的请求的访问路径，没有检查是否有重复
     */
    String[] urlPatterns();

    /**
     * 请求方式，默认支持4种常见的请求方式。HTTP请求一共有8个
     */
    String[] type() default {RequestType.GET, RequestType.POST, RequestType.PUT, RequestType.DELETE};

    /**
     * 优先级
     * @return
     */
    int priority() default Integer.MAX_VALUE;
}
