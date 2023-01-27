package com.cooperation.record.dao.impl;

import com.cooperation.record.dao.Mapper.SimpleBaseMapper;
import com.cooperation.record.dao.RecordDao;
import com.cooperation.record.domain.pojo.Record;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author cyl
 * @date 2021/10/16
 */
public class RecordDaoImpl extends SimpleBaseMapper<Record> implements RecordDao {

    @Override
    public String getTableName() {
        return "record";
    }

    /**
     * 因为该类的查某一个用户的记录交给了getSelect来处理，所以这里就没有用了
     * 当复杂的查询不只是一个时，还是可以使用的
     * @param object
     * @return
     */
    @Override
    public String getQueryCondition(Record object, List<Object> list) {
        list.add(object.getRecordId());
        return "recordId = ?" ;
    }

    @Override
    public String getSelect(Map<String, Object> map, List<Object> list) {
        // 该方法拼接的是where后面的部分，也就是where userId = {} limit {};
        if(map.get("allCatalogue") != null) {
            String base = "userId = ? and catalogue like ?";
            list.add(map.get("userId"));
            list.add(map.get("catalogue") + "%");
            return base;
        } else if(map.get("search") != null && "1".equals(map.get("type"))){
            int page = (Integer.parseInt((String)map.get("page"))-1)*15;
            String base = "userId = ? and title like ? limit ?,?";
            list.add(map.get("userId"));
            list.add("%"+map.get("search")+"%");
            list.add(page);
            list.add(15);
            return base;
        } else if (map.get("search") != null && "2".equals(map.get("type"))) {
            int page = (Integer.parseInt(map.get("page").toString())-1)*15 + Integer.parseInt(map.get("pageBefore").toString());
            String base = "userId = ? and novelTxt like ? order by recordId desc limit ?,?";
            list.add(map.get("userId"));
            list.add("%"+map.get("search")+"%");
            list.add(page);
            list.add(15);
            return base;
        } else if (map.get("search") != null && "4".equals(map.get("type"))) {
            int page = (Integer.parseInt(map.get("page").toString())-1)*15;
            String base = "userId = ? and novelTxt like ? limit ?,?";
            list.add(map.get("userId"));
            list.add("%"+map.get("search")+"%");
            list.add(page);
            list.add(15);
            return base;
        } else if (map.get("search") != null && "3".equals(map.get("type"))) {
            String base = "userId = ? and title like ?";
            list.add(map.get("userId"));
            list.add("%"+map.get("search")+"%");
            return base;
        } else if (map.get("pageBefore") != null && map.get("catalogue") != null) {
            String base = "userId = ? and catalogue = ? order by recordId desc limit ?,? ";
            int page = (Integer.parseInt(map.get("page").toString())-1)*15 + Integer.parseInt(map.get("pageBefore").toString());
            list.add(map.get("userId"));
            list.add(map.get("catalogue"));
            list.add(page);
            list.add(15);
            return base;
        } else if (map.get("userId") != null && map.get("catalogue") != null) {
            String base = "userId = ? and catalogue = ? order by recordId desc";
            list.add(map.get("userId"));
            list.add(map.get("catalogue"));
            return base;
        }
        return null;
    }
}
