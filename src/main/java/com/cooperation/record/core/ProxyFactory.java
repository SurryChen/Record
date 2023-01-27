package com.cooperation.record.core;

import java.lang.reflect.Proxy;

/**
 * 代理工厂，可以在这里获取一个代理对象
 * Proxy返回的是一个Object的对象，所以需要强制转换为<T>
 *
 * @author cyl
 * @date 2021/10/11
 */
@SuppressWarnings("unchecked")
public class ProxyFactory {

    public static <T> T getProxyForTransaction(T toBeProxy) {
        return (T) Proxy.newProxyInstance(toBeProxy.getClass().getClassLoader(),
                toBeProxy.getClass().getInterfaces(), ServiceTransactionProxy.create(toBeProxy));
    }

}
