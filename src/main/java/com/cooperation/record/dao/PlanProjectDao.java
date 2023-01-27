package com.cooperation.record.dao;

import com.cooperation.record.domain.pojo.PlanProject;

import java.util.List;
import java.util.Map;

/**
 * 定义对planProject表的操作
 * 不能同时定义plan的，因为SimpleBaseMapper中一次只能传参一个进去
 * @author cyl
 * @date 2021/10/22
 */
public interface PlanProjectDao {

    /**
     * 增一个计划项目
     * 传入一个计划项目的javabean
     */
    public void insert(PlanProject planProject) throws Exception;

    /**
     * 删除一个计划项目
     * 需要使用planProjectId
     */
    public void delete(PlanProject planProject) throws Exception;

    /**
     * 修改计划内容
     * 需要使用到planProjectId
     */
    public void update(PlanProject planProject) throws Exception;

    /**
     * 修改计划内容
     * 需要使用到planProjectId
     */
    public void update(Map<String, Object> map, PlanProject planProject) throws Exception;

    /**
     * 根据userId查询内容，并且可以设置getSelect()丰富一下功能
     */
    public List<Map<String,Object>> select(Map<String,Object> map) throws Exception;

    /**
     * 根据userId查询内容，并且可以设置getSelect()丰富一下功能
     */
    public Map<String, Object> select(PlanProject planProject) throws Exception;

}
