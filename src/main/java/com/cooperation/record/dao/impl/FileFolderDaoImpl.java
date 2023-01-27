package com.cooperation.record.dao.impl;

import com.cooperation.record.dao.FileFolderDao;
import com.cooperation.record.dao.Mapper.SimpleBaseMapper;
import com.cooperation.record.domain.pojo.FileFolder;

import java.util.List;
import java.util.Map;

/**
 * @author cyl
 * @date 2021/11/17
 */
public class FileFolderDaoImpl extends SimpleBaseMapper<FileFolder> implements FileFolderDao {


    @Override
    public String getTableName() {
        return "FileFolder";
    }

    @Override
    public String getQueryCondition(FileFolder object, List<Object> list) {
        list.add(object.getUserId());
        list.add(object.getAheadFolderName());
        list.add(object.getFileFolderName());
        return "userId = ? and aheadFolderName = ? and fileFolderName = ?";
    }

    /**
     * getSelect里面是唯一标识
     * @param map
     * @param list
     * @return
     */
    @Override
    public String getSelect(Map<String, Object> map, List<Object> list) {
        if(map.get("like") != null) {
            list.add(map.get("userId"));
            list.add(map.get("aheadFolderName") + "%");
            return "userId = ? and aheadFolderName like ?";
        }
        if(map.get("userId") != null && map.get("aheadFolderName") != null){
            list.add(map.get("userId"));
            list.add(map.get("aheadFolderName"));
            return "userId = ? and aheadFolderName = ?";
        } else if (map.get("catalogue") != null && map.get("page") != null) {
            String base = "userId = ? and aheadFolderName = ? limit ?,?";
            int page = (Integer.parseInt((String)map.get("page"))-1)*15;
            list.add(map.get("userId"));
            list.add(map.get("catalogue"));
            list.add(page);
            list.add(15);
            return base;
        } else if (map.get("catalogue") != null) {
            String base = "userId = ? and aheadFolderName = ?";
            list.add(map.get("userId"));
            list.add(map.get("catalogue"));
            return base;
        }
        return null;
    }
}
