package com.cooperation.record.utils;

/**
 * @author wtk
 * @description
 * @date 2021-09-14
 */
public class StringUtil {
    /**
     * 截取类名，首字符小写
     * @param classPath 类路径
     * @return
     */
    public static String getLowerClassNameByPath(String classPath) {
        String className = classPath.substring(classPath.lastIndexOf(".") + 1);
        return getLowerClassName(className);
    }

    /**
     * 类名首字符小写
     * @param className 类路径
     * @return
     */
    public static String getLowerClassName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
