package com.cooperation.record.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回信息到客户端的枚举类型
 * 不加注释会加黄显示一块区域
 * @author cyl
 * @date 2021/10/11
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
@Getter
@AllArgsConstructor
public enum ApiInfo {

    MAILBOX_NOTFOUND(1001,"该邮箱未注册"),
    PASSWORD_WRONG(1002,"密码错误"),
    CHECK_WRONG(1003,"验证码错误"),
    LOGIN_SUCCESS(1004,"登录成功"),
    REGISTER_SUCCESS(1005,"注册成功"),
    PASSWORD_MODIFY_SUCCESS(1006,"修改密码成功"),
    MAILBOX_FOUND(1007,"该邮箱已被注册"),
    SUCCESS(1008,"操作成功"),

    WRONG(1009,"参数缺失，操作失败"),
    TYPE_ERROR(1010,"文件格式错误"),
    File_OVERLOAD(1011,"文件过大");

    int code;
    String message;
}
