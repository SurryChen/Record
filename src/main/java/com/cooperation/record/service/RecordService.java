package com.cooperation.record.service;

import com.cooperation.record.domain.pojo.FileFolder;
import com.cooperation.record.domain.pojo.Record;
import com.cooperation.record.domain.vo.ApiMsg;

import java.util.List;
import java.util.Map;

/**
 * 定义好能进行的操作
 * @author cyl
 * @date 2021/10/16
 */
public interface RecordService {

    /** 保存文章信息 */
    public ApiMsg<Record> saveRecord(Record record) throws Exception;

    /** 获取用户记录信息 */
    public ApiMsg<List<Record>> requireRecord(Map<String,Object> map) throws Exception;

    /** 搜索功能 */
    public ApiMsg<List<Record>> searchRecord(Map<String, Object> map) throws Exception;

    /** 删除功能 */
    public ApiMsg<Record> deleteRecord(Record record) throws Exception;

    /**
     * 对文件夹操作的功能
     * @param fileFolder
     * @param operation
     * @return
     */
    ApiMsg fileFolder(FileFolder fileFolder, String operation, String update) throws Exception;

    /**
     * 获取一棵文件路径树
     * @param fileFolder
     * @return
     */
    ApiMsg requireFileTree(FileFolder fileFolder) throws Exception;

    /**
     * 获取所有记录信息
     * @param map
     * @return
     */
    ApiMsg<List<Record>> requireRecordAll(Map<String, Object> map);
}
