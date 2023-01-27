package com.cooperation.record.web.servlet.mvc.exeption;

/**
 * @description 构造Bean异常
 */
public class BeanException extends RuntimeException {

    public BeanException() {
        super("【构造Bean异常】");
    }

    public BeanException(String message) {
        super("【构造Bean异常】" + message);
    }
}
