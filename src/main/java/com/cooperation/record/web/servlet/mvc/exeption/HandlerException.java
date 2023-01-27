package com.cooperation.record.web.servlet.mvc.exeption;

/**
  * @description Handler异常
 */
public class HandlerException extends Exception {

    public HandlerException() {
        super("Handler异常");
    }

    public HandlerException(String message) {
        super("Handler异常：" + message);
    }
}
