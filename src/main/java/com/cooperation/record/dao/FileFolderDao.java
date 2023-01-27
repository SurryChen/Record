package com.cooperation.record.dao;

import com.cooperation.record.domain.pojo.DayPlan;
import com.cooperation.record.domain.pojo.FileFolder;

import java.util.List;
import java.util.Map;

/**
 * @author cyl
 * @date 2021
 */
public interface FileFolderDao {

    /**
     * 增加一个文件夹
     */
    public void insert(FileFolder fileFolder) throws Exception;

    /**
     * 删除一个文件夹
     */
    public void delete(Map<String, Object> map) throws Exception;

    /**
     * 修改一个文件夹
     */
    public void update(FileFolder fileFolder) throws Exception;

    /**
     * 修改其余元组目录
     */
    public void update(Map<String, Object> map, FileFolder fileFolder) throws Exception;

    /**
     * 查找文件夹
     */
    public List<Map<String,Object>> select(Map<String, Object> map) throws Exception;


}
