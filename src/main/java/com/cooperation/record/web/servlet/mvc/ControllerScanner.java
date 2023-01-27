package com.cooperation.record.web.servlet.mvc;

import com.cooperation.record.utils.StringUtil;
import com.cooperation.record.web.servlet.mvc.annotation.ServletHandler;
import com.cooperation.record.web.servlet.mvc.exeption.BeanException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @description 扫描controller包下的Controller类
 */
public class ControllerScanner {
    /**
     * 这个是Controller包所在的位置
     */
    private static String scanPath = "com/cooperation/record/web/servlet";

    /**
     * 获取所有Controller及其方法的信息列表
     */
    public static List<ControllerInfo<?>> doScan() {
        URL resource = ControllerScanner.class.getClassLoader().getResource(scanPath);
        List<ControllerInfo<?>> infoList = new ArrayList<>();
        try {
            if (resource != null && resource.toString().startsWith("file")) {

                String filePath = URLDecoder.decode(resource.getFile(), "utf-8");

                File dir = new File(filePath);
                // 遍历包下的所有文件
                List<File> fileList = fetchFileList(dir);
                for (File f : fileList) {
                    String classPath = getClassPath(f);
                    if (classPath == null) {
                        continue;
                    }
                    Class<?> clazz = Class.forName(classPath);

                    // 没有使用ServletHandler，则跳过
                    if (!clazz.isAnnotationPresent(ServletHandler.class)) {
                        continue;
                    }

                    // 获取类名，首字母小写
                    String className = StringUtil.getLowerClassNameByPath(classPath);

                    // 构造单例对象并保存
                    BeanFactory.getBean(className, clazz);
                    // 反射注解，获取信息
                    ControllerInfo<?> handlerInfo = initControllerInfo(clazz);
                    infoList.add(handlerInfo);
                }
            }
            // 按优先级排序
            sortByPriority(infoList);
            return infoList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanException("扫描Controller出现异常");
        }
    }

    /**
     * 递归获取包下所有的文件
     * @param dir 路径
     */
    private static List<File> fetchFileList(File dir) {
        List<File> fileList = new ArrayList<>();
        fetchFileList(dir, fileList);
        return fileList;
    }

    /**
     * 递归方法
     * @param dir 路径
     */
    private static void fetchFileList(File dir, List<File> fileList) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                fetchFileList(f, fileList);
            }
        } else {
            fileList.add(dir);
        }
    }

    /**
     * 截取class文件的类路径
     * @param f
     * @return
     */
    private static String getClassPath(File f) {
        String fileName = f.getAbsolutePath();
        if (fileName.endsWith(".class")) {
            String nosuffixFileName = fileName.substring(8 + fileName.lastIndexOf("classes"), fileName.indexOf(".class"));
            // 将路径中的“/”替换成类路径中的“.”
            String classPath = nosuffixFileName.replaceAll("\\\\", ".");
            return classPath;
        }
        return null;
    }

    /**
     * 通过反射获取注解的数据
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> ControllerInfo<T> initControllerInfo(Class<T> clazz) {
        /*
        反射Controller类
         */
        ServletHandler annotation = clazz.getAnnotation(ServletHandler.class);
        String clazzSimpleName = clazz.getSimpleName();
        String clazzPathName = clazz.getName();

        String[] urlPaths = annotation.value();
        if (urlPaths.length == 0) {
            urlPaths = annotation.urlPatterns();
        }
        if (urlPaths.length == 0) {
            throw new BeanException("Controller没有设置访问路径");
        }

        /*
         反射Controller类的方法
         */
        HandlerInfo controllerHandlerInfo = new HandlerInfo(clazzSimpleName, clazzPathName, urlPaths, annotation.priority(), annotation.type());

        List<HandlerInfo> methodHandlerInfos = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(ServletHandler.class)) {
                continue;
            }
            annotation = method.getAnnotation(ServletHandler.class);
            urlPaths = annotation.value();
            if (urlPaths.length == 0) {
                urlPaths = annotation.urlPatterns();
            }
            if (urlPaths.length == 0) {
                continue;
            }
            HandlerInfo methodHandlerInfo = new HandlerInfo(
                    clazzSimpleName, clazzPathName + "#" + method.getName(),
                    urlPaths, annotation.priority(), annotation.type(), method);
            methodHandlerInfos.add(methodHandlerInfo);
        }

        ControllerInfo<T> controllerInfo = new ControllerInfo<>(
                StringUtil.getLowerClassName(clazz.getSimpleName()), clazz,
                controllerHandlerInfo, methodHandlerInfos);
        return controllerInfo;
    }

    /**
     * 根据优先级排序。先匹配到的直接执行
     * @param infos
     */
    private static void sortByPriority(List<ControllerInfo<?>> infos) {
        // 对Controller的方法的优先级排序
        for (ControllerInfo<?> info : infos) {
            List<HandlerInfo> methodHandlerInfo = info.getMethodHandlerInfos();
            // Comparator是比较器，HandlerInfo::getPriority是方法引用。
            methodHandlerInfo.sort(Comparator.comparingInt(HandlerInfo::getPriority));
        }
        // 对Controller的优先级排序。lambda表达式
        infos.sort((Comparator.comparingInt(o -> o.getControllerHandlerInfo().getPriority())));
    }
}
