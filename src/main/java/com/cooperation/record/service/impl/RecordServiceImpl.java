package com.cooperation.record.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.dao.FileFolderDao;
import com.cooperation.record.dao.RecordDao;
import com.cooperation.record.dao.impl.FileFolderDaoImpl;
import com.cooperation.record.dao.impl.RecordDaoImpl;
import com.cooperation.record.domain.pojo.FileFolder;
import com.cooperation.record.domain.pojo.Record;
import com.cooperation.record.domain.vo.ApiMsg;
import com.cooperation.record.service.RecordService;
import com.cooperation.record.utils.AddColor;
import org.apache.commons.beanutils.BeanUtils;

import java.util.*;

/**
 * @author cyl
 * @date 2021/11/21
 */
public class RecordServiceImpl implements RecordService {

    RecordDao recordDao = new RecordDaoImpl();
    FileFolderDao fileFolderDao = new FileFolderDaoImpl();

    @Override
    public ApiMsg<Record> saveRecord(Record record) throws Exception {
        // 先要判断编号是否为0，如果为0，则是新建，如果不为0，则是更改
        System.out.println("编号是"+record.getRecordId());
        if(record.getRecordId() == 0){
            recordDao.insert(record);
        } else {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("userId", record.getUserId());
                map.put("title", record.getTitle());
                map.put("novel", record.getNovel());
                map.put("novelTxt",record.getNovelTxt());
                map.put("createTime", record.getCreateTime());
                map.put("updateTime", record.getUpdateTime());
                map.put("overheadTime", record.getOverheadTime());
                recordDao.update(map,record);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        return apiMsg;
    }

    @Override
    public ApiMsg<List<Record>> requireRecord(Map<String,Object> map) throws Exception {
        try {
            // map里面的内容有userId，page,catalogue
            // 如果页数过多，返回值为[]
            List<Map<String, Object>> listFile = fileFolderDao.select(map);
            ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
            if (listFile.size() == 15) {
                // #发送页数，查找条数，发现满足15条，返回即可
                // 将List<Map<String,Object>>转成List<FileFolder>
                List<FileFolder> fileList = new ArrayList<>();
                for (int i = 0; i < listFile.size(); i++) {
                    Map<String, Object> beanMap = listFile.get(i);
                    FileFolder fileFolder = new FileFolder();
                    BeanUtils.populate(fileFolder, beanMap);
                    fileList.add(fileFolder);
                }
                apiMsg.setData(fileList);
                return apiMsg;
            } else if (listFile.size() < 15 && listFile.size() != 0) {
                // #交接处，发送页数，查找条数，发现不满足15条，查找文章表，获取第一页，拿到15条数据，选择前5条
                List<Map<String, Object>> listRecord = recordDao.select(map);
                // 将数据合并
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < listFile.size(); i++) {
                    Map<String, Object> beanMap = listFile.get(i);
                    FileFolder fileFolder = new FileFolder();
                    BeanUtils.populate(fileFolder, beanMap);
                    list.add(fileFolder);
                }
                for (int i = 0; i < 15 - listFile.size(); i++) {
                    // 有可能记录数比较少
                    if (i == listRecord.size()) {
                        break;
                    }
                    Map<String, Object> beanMap = listRecord.get(i);
                    Record record = new Record();
                    BeanUtils.populate(record, beanMap);
                    list.add(record);
                }
                apiMsg.setData(list);
                return apiMsg;
            } else if (listFile.size() == 0) {
                // 因为还查找总数，所以需要新建一个map
                Map<String, Object> mapMidden = new HashMap<>();
                mapMidden.put("userId", map.get("userId"));
                mapMidden.put("aheadFolderName", map.get("catalogue"));
                // #交接后，发送页数，查找条数，发现文件夹表为0，查找文章表，获取第几页的数据或者说第几条的数据，可以直接查找文件夹中有多少个文件，获取到有100,100/15=6,余10，15-10，所以条数就是5+[（page-6-1）-1]* 15
                int fileNumber = fileFolderDao.select(mapMidden).size();
                // 100/15=6,余10，15-10，所以条数就是5+[（page-6-1）-1]* 15
                // one是前面文件夹所在的页数
                int one = (fileNumber / 15) + 1;
                // two是文件夹页转换到文章页的页中记录的页数
                int two = 15 - (fileNumber % 15);
                // 所要查找的文章页数
                int page = Integer.parseInt((String) map.get("page")) - one;
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("userId", map.get("userId"));
                stringObjectMap.put("catalogue", map.get("catalogue"));
                stringObjectMap.put("pageBefore", two);
                stringObjectMap.put("page", page);
                List<Map<String, Object>> objectMap = recordDao.select(stringObjectMap);
                // 开始转换
                List<Record> recordList = new ArrayList<>();
                for (int i = 0; i < objectMap.size(); i++) {
                    Map<String, Object> beanMap = objectMap.get(i);
                    Record record = new Record();
                    BeanUtils.populate(record, beanMap);
                    recordList.add(record);
                }
                apiMsg.setData(recordList);
                return apiMsg;
            } else {
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiMsg<List<Record>> searchRecord(Map<String, Object> map) throws Exception {
        // 如果页数过多，返回值为[]
        // 搜索标题是1，搜索内容是2
        map.put("type","1");
        // 开始检索标题
        List<Map<String,Object>> list = recordDao.select(map);
        // 分三种情况，一是等于15条，二是少于15条，但是不为0，三是为0
        // 设置一下保存信息的APIMsg
        ApiMsg<List<Record>> apiMsg = new ApiMsg<>(ApiInfo.SUCCESS);
        // 将List<Map<String,Object>>转成List<Record>
        List<Record> recordList = new ArrayList<>();
        if (list.size() == 15) {
            // 直接获取保存即可
            for(int i = 0;i < list.size();i++){
                Map<String,Object> beanMap = list.get(i);
                Record record = new Record();
                BeanUtils.populate(record,beanMap);
                record.setTitle(AddColor.addColorTitle(record.getTitle(),map.get("search").toString()));
                recordList.add(record);
            }
        } else if (list.size() < 15 && list.size() > 0) {
            // 先保存标题的部分，再保存剩余的文章检索部分
            for(int i = 0;i < list.size();i++){
                Map<String,Object> beanMap = list.get(i);
                Record record = new Record();
                BeanUtils.populate(record,beanMap);
                record.setTitle(AddColor.addColorTitle(record.getTitle(),map.get("search").toString()));
                recordList.add(record);
            }
            // 开始查找文章内容部分
            map.put("type","4");
            map.put("page",1);
            List<Map<String,Object>> listObject = recordDao.select(map);
            // 开始放入到APIMsg中
            for (int i = 0; i < 15 - list.size(); i++) {
                // 有可能记录数比较少
                if (i == listObject.size()) {
                    break;
                }
                Map<String, Object> beanMap = listObject.get(i);
                Record record = new Record();
                BeanUtils.populate(record, beanMap);
                record.setNovelTxt(AddColor.addColorNovel(record.getNovelTxt(),map.get("search").toString()));
                recordList.add(record);
            }
        } else if (list.size() == 0) {
            // 先使用标题查找一下看看总个数
            map.put("type","3");
            System.out.println("--------------------------"+map.get("search"));
            System.out.println("-------------------------进入的是0");
            List<Map<String, Object>> mapList = recordDao.select(map);
            // 获取条数
            int fileNumber = mapList.size();
            // 100/15=6,余10，15-10，所以条数就是5+[（page-6-1）-1]* 15
            // one是前面文件夹所在的页数
            int one = (fileNumber / 15) + 1;
            // two是文件夹页转换到文章页的页中记录的页数
            int two = 15 - (fileNumber % 15);
            // 所要查找的文章页数
            int page = Integer.parseInt((String) map.get("page")) - one;
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("userId", map.get("userId"));
            stringObjectMap.put("pageBefore", two);
            stringObjectMap.put("page", page);
            stringObjectMap.put("type","2");
            stringObjectMap.put("search",map.get("search"));
            List<Map<String, Object>> objectMap = recordDao.select(stringObjectMap);
            for (int i = 0; i < objectMap.size(); i++) {
                Map<String, Object> beanMap = objectMap.get(i);
                Record record = new Record();
                BeanUtils.populate(record, beanMap);
                record.setNovelTxt(AddColor.addColorNovel(record.getNovelTxt(),map.get("search").toString()));
                recordList.add(record);
            }
        }
        apiMsg.setData(recordList);
        return apiMsg;
    }

    @Override
    public ApiMsg<Record> deleteRecord(Record record) throws Exception {
        recordDao.delete(record);
        return new ApiMsg<>(ApiInfo.SUCCESS);
    }

    /**
     * 对文件夹进行操作的功能
     * @param fileFolder
     * @param operation
     * @return
     */
    @Override
    public ApiMsg fileFolder(FileFolder fileFolder, String operation, String update) throws Exception {
        // “operation”:”0”（0就是创建，1就是删除，2就是修改）
        if (operation.equals("0")) {
            fileFolderDao.insert(fileFolder);
        } else if (operation.equals("1")) {
            try {
                String fileFolderName = fileFolder.getFileFolderName();
                deleteFileFolder(fileFolder.getUserId(), fileFolder.getAheadFolderName() + fileFolderName + "/");
                // 删除文件夹
                Map<String, Object> map = new HashMap<>();
                map.put("userId", fileFolder.getUserId());
                map.put("fileFolderName", fileFolder.getFileFolderName());
                map.put("aheadFolderName", fileFolder.getAheadFolderName());
                fileFolderDao.delete(map);
                // 删除文章
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("userId", fileFolder.getUserId());
                objectMap.put("catalogue", fileFolder.getAheadFolderName() + fileFolderName + "/");
                recordDao.delete(objectMap);
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operation.equals("2")) {
            // 先把/a路径下的所有文件都查出来
            // 获取文件夹路径
            try {
                String catalogue = fileFolder.getAheadFolderName() + fileFolder.getFileFolderName() + "/";
                String catalogueUpdate = fileFolder.getAheadFolderName() + update + "/";
                Map<String, Object> map = new HashMap<>();
                map.put("userId", fileFolder.getUserId());
                map.put("catalogue", catalogue);
                map.put("allCatalogue", "yes");
                // 对文章要使用模糊查询
                List<Map<String, Object>> list = recordDao.select(map);
                for (int i = 0; i < list.size(); i++) {
                    // 拿到一个元组的map集合
                    Map<String, Object> objectMap = list.get(i);
                    // 拿到原先的路径
                    String catalogueOne = (String) objectMap.get("catalogue");
                    // 替换途径，修改值
                    String catalogueUpdateReplace = catalogueOne.replaceFirst(catalogue, catalogueUpdate);
                    objectMap.put("catalogue", catalogueUpdateReplace);
                    Record record = new Record();
                    BeanUtils.populate(record, objectMap);
                    // 更新值
                    recordDao.update(record);
                }
                // 对文件夹进行模糊查询
                Map<String, Object> mapFile = new HashMap<>();
                mapFile.put("userId", fileFolder.getUserId());
                mapFile.put("aheadFolderName", catalogue);
                mapFile.put("like","yes");
                System.out.println(catalogue);
                List<Map<String, Object>> mapList = fileFolderDao.select(mapFile);
                System.out.println("--------------------------");
                System.out.println(catalogue);
                System.out.println(mapList.size());
                System.out.println("--------------------------");
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> objectMap = mapList.get(i);
                    // 转换成一个对象
                    FileFolder fileFolderMidden = new FileFolder();
                    BeanUtils.populate(fileFolderMidden, objectMap);
                    // 拿到原先的路径
                    String catalogueOne = (String) objectMap.get("aheadFolderName");
                    // 替换途径，修改值
                    String catalogueUpdateReplace = catalogueOne.replaceFirst(catalogue, catalogueUpdate);
                    // 要更新的内容
                    Map<String, Object> mapMidden = new HashMap<>();
                    mapMidden.put("aheadFolderName",catalogueUpdateReplace);
                    // 更新
                    fileFolderDao.update(mapMidden, fileFolderMidden);
                }
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("fileFolderName", update);
                fileFolderDao.update(objectMap,fileFolder);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ApiMsg(ApiInfo.SUCCESS);
    }

    @Override
    public ApiMsg requireFileTree(FileFolder fileFolder) throws Exception {
        JSONObject jsonObject = jsonObject(fileFolder.getUserId(),fileFolder.getAheadFolderName(),fileFolder.getFileFolderName());
        ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
        apiMsg.setData(jsonObject);
        return apiMsg;
    }

    @Override
    public ApiMsg<List<Record>> requireRecordAll(Map<String, Object> map) {
        try {
            // map里面的内容有userId,catalogue
            // 如果页数过多，返回值为[]
            // 查找所有，一起加入
            List<Map<String, Object>> listFile = fileFolderDao.select(map);
            List list = new ArrayList();
            for (int i = 0;i < listFile.size();i++) {
                Map<String, Object> beanMap = listFile.get(i);
                FileFolder fileFolder = new FileFolder();
                BeanUtils.populate(fileFolder, beanMap);
                list.add(fileFolder);
            }
            List<Map<String, Object>> listRecord = recordDao.select(map);
            for (int i = 0;i < listRecord.size();i++) {
                Map<String, Object> beanMap = listRecord.get(i);
                Record record = new Record();
                BeanUtils.populate(record, beanMap);
                list.add(record);
            }
            ApiMsg apiMsg = new ApiMsg(ApiInfo.SUCCESS);
            apiMsg.setData(list);
            return apiMsg;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件夹的方法
     * 返回true说明，已经没有下一级的文件夹了
     * 一是原先就没有，二是删除后就没有了
     * @param userId
     * @param catalogue
     * @return
     * @throws Exception
     */
    public boolean deleteFileFolder (int userId, String catalogue) throws Exception {
        // 使用catalogue去查找数据，如果有就使用for循环一个一个遍历，如果没有，删除然后返回一个true
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("aheadFolderName",catalogue);
        // map里面是选择条件
        List<Map<String, Object>> list = fileFolderDao.select(map);
        if(list.isEmpty()) {
            // 说明没有下一个文件夹了
            return true;
        } else {
            // 获取下一个路径，开始循环
            for(int i = 0;i < list.size();i++) {
                Map<String, Object> objectMap = list.get(i);
                String catalogueOther = (String)objectMap.get("fileFolderName");
                String catalogueOtherTwo = catalogue + catalogueOther + "/";
                if(deleteFileFolder(userId,catalogueOtherTwo)) {
                    // 删除现在的文件夹
                    Map<String, Object> stringObjectMap = new HashMap<>();
                    stringObjectMap.put("userId",userId);
                    stringObjectMap.put("fileFolderName",catalogueOther);
                    stringObjectMap.put("aheadFolderName",catalogue);
                    // 说明没有了，可以删除这个文件夹了
                    fileFolderDao.delete(map);
                    // 删除文件夹后，删除这个文件夹下的文件
                    Map<String, Object> mapRecord = new HashMap<>();
                    mapRecord.put("userId",userId);
                    mapRecord.put("catalogue",catalogueOtherTwo);
                    recordDao.delete(mapRecord);
                }
            }
            return true;
        }
    }

    /**
     * 生成一个文件树的JSON字符串
     * @return
     */
    public JSONObject jsonObject (int userId, String catalogue, String fileName) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("aheadFolderName",catalogue + fileName + "/");
        // map里面是选择条件
        List<Map<String, Object>> list = fileFolderDao.select(map);
        if(list.isEmpty()) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("userId",userId);
            objectMap.put("catalogue",catalogue + fileName + "/");
            List<Map<String, Object>> mapList = recordDao.select(objectMap);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(int i = 0;i < mapList.size();i++) {
                Map<String, Object> midden = mapList.get(i);
                jsonArray.add(midden.get("title"));
            }
            jsonObject.put(fileName,jsonArray);
            return jsonObject;
        } else {
            // 创建一个JSONObject对象，该JSONObject对象的键名是文件名，值是文件下的文件
            JSONObject jsonObject = new JSONObject();
            // 创建一个JSONOArray对象，接收返回的JSONObject对象
            JSONArray jsonArray = new JSONArray();
            // 添加文件
            for (int i = 0;i < list.size();i++) {
                // 获取到一个元组
                Map<String, Object> midden = list.get(i);
                // 拿到文件名和文件路径
                String fileNameOther = (String)midden.get("fileFolderName");
                String catalogueOther = (String)midden.get("aheadFolderName");
                // 放置到jsonArray里面
                jsonArray.add(jsonObject(userId,catalogueOther,fileNameOther));
            }
            // 添加文章
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("userId",userId);
            objectMap.put("catalogue",catalogue + fileName + "/");
            List<Map<String, Object>> mapList = recordDao.select(objectMap);
            for (int i = 0;i < mapList.size();i++) {
                Map<String, Object> midden = mapList.get(i);
                jsonArray.add(midden.get("title"));
            }
            jsonObject.put(fileName,jsonArray);
            return jsonObject;
        }
    }
}
