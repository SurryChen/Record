package com.cooperation.record.core;

import com.cooperation.record.utils.JdbcUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理
 * 这里主要用于处理事务回滚
 * 将事务回滚操作写在invoke中
 *
 * @author cyl
 * @date 2021/10/11
 */
public class ServiceTransactionProxy implements InvocationHandler {

    // 构造方法私有化
    /**
     * 要代理的对象
     */
    private final Object serviceRunner;

    private ServiceTransactionProxy(Object serviceRunner) {
        this.serviceRunner = serviceRunner;
    }

    // 对象的创建，对象的创建需要导入想要代理的对象

    /**
     * 创建对象
     */
    public static <T> ServiceTransactionProxy create(T serviceRunner) {
        return new ServiceTransactionProxy(serviceRunner);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 因为创建类的时候传入了要代理的对象，所以这里的proxy就没有用处
        Object value = null;
        // 事务开启
        try {
            JdbcUtil.beginTransaction();
            value = method.invoke(serviceRunner, args);
        } catch (Exception e) {
            // 事务回滚
            JdbcUtil.rollbackTransaction();
        } finally {
            // 事务提交
            JdbcUtil.commitTransaction();
            // 事务关闭
            JdbcUtil.closeTransaction();
            System.out.println("事务关闭没问题");
        }
        return value;
    }
}
