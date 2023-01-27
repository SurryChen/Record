package com.cooperation.record.service.Factory;

import com.cooperation.record.core.ProxyFactory;
import com.cooperation.record.service.PlanService;
import com.cooperation.record.service.RecordService;
import com.cooperation.record.service.UserService;
import com.cooperation.record.service.impl.PlanServiceImpl;
import com.cooperation.record.service.impl.RecordServiceImpl;
import com.cooperation.record.service.impl.UserServiceImpl;

/**
 * 代理service层的工厂
 *
 * @author cyl
 * @date 2021/10/11
 */
public class ServiceProxyFactory {

    /**
     * 获取代理后的UserService的实现类
     * 想要代理的类是新建的
     * 新建过程隐藏起来，不会暴露到使用过程中
     */
    public static UserService getUserService() {
        return ProxyFactory.getProxyForTransaction(new UserServiceImpl());
    }

    /**
     * 获取代理后的RecordService的实现类
     */
    public static RecordService getRecordService() {
        return ProxyFactory.getProxyForTransaction(new RecordServiceImpl());
    }

    /**
     * 获取代理后的RecordService的实现类
     */
    public static PlanService getPlanService() {
        return ProxyFactory.getProxyForTransaction(new PlanServiceImpl());
    }
}
