package com.cooperation.record.utils;

import com.cooperation.record.domain.pojo.User;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 拼接SQL中的语句的工具类
 * 这里有一个问题，就是没有将前端的东西转成转义一下，字符转义就交给前端来做了
 * 里面反射的内容可以写一个工具类，返回一个Map集合可以更好操作
 * @author cyl
 * @date 2021/10/07
 */
public class LinkSqlUtil {

    /**
     * 使用反射获取对象中的变量名，并根据名字顺序依次拼接变量的值
     * @param obj 传入pojo对象
     * @return 返回拼接而成的语句
     */
    public static String insertValueLinkSql(Object obj) throws IllegalAccessException {
        /**
         * 忘了直接拼接值会出现注入问题
         * ?,?,?,?,?,?,?,?,?
         * 返回一串问号
         */
        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuilder value = new StringBuilder();
        int i = fields.length;
        for(int j = 0;j < i;j++){
            if(j == i-1){
                value.append("?");
            } else {
                value.append("?,");
            }
        }
        return value.toString();
    }

    /**
     * 通过反射获取变量名，并拼接
     * @param user
     * @return
     */
    public String insertNameLinkSql(User user){
        return "0";
    }

    /**
     * 拼接set中的内容，使用对象
     * xxx = ? ， xxx = ?
     * @param object
     * @return 返回一个拼接好的字符串
     */
    public static String updateSetLinkSql(Object object) throws IllegalAccessException {

        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder updateSet = new StringBuilder();
        int i = fields.length;
        int j = 0;
        for(Field field:fields){
            field.setAccessible(true);
            j++;
            if(j == i){
                updateSet.append("" + field.getName() + " = ? ");
            } else {
                updateSet.append("" + field.getName() + " = ? ,");
            }
        }
        return updateSet.toString();
    }

    /**
     * 拼接set中的内容，使用键值对
     * xxx = ? ， xxx = ?
     * 遇到的问题是，拼接语句需要注意数据类型和null不要加双引号
     * @param map
     * @return 返回一个拼接好的字符串
     */
    public static String updateMapLinkSql(Map<String,Object> map) throws IllegalAccessException {
        StringBuilder linkSql = new StringBuilder();
        int size = map.size();
        int i = 0;
        for(String key:map.keySet()){
            i++;
            if(i == size){
                linkSql.append(key+" = ?");
                break;
            }
            linkSql.append(key+" = ? , ");
        }
        return linkSql.toString();
    }

    /**
     * 拼接where中的内容
     * xxx = xxx
     * 下面的拼接语句仅适用于该项目（主要是只需要匹配一个字段就可以了）
     * 必然是int类型，所以不用担心注入问题
     * @param object
     * @return 返回一个拼接好的字符串
     */
    public static String updateWhereLinkSql(Object object,String tableName) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = object.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        return tableName+"Id="+fields[0].get(object);
    }

    /**
     * 拼接delete语句中的where部分，主要就是返回表的id即可，同上
     * 必然是int类型，所以不用担心注入问题
     * @param object
     * @return 返回一个拼接好的字符串
     */
    public static String deleteWhereLinkSql(Object object,String tableName) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = object.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        return tableName+"Id="+fields[0].get(object);
    }

    /**
     * 使用Map集合的键值对拼接，拼接where中的，格式为xxx = ? and xxx = ?
     * 之前有一个比较大的问题就是，无法使用了String来强转类型，导致int类型转换出错
     * @param maps 想要拼接的键值对
     * @return 返回一个拼接好的字符串
     */
    public static String mapWhereLinkSql(Map<String,Object> maps) {
        StringBuilder linkSql = new StringBuilder();
        int size = maps.size();
        int i = 0;
        for(String key:maps.keySet()){
            i++;
            if(i == size){
                linkSql.append(key+" = ?");
                break;
            }
            linkSql.append(key+" = ? and ");
        }
        return linkSql.toString();
    }

    /**
     * 使用Map集合的键值对拼接，格式为 xxx,xxx，主要是select中的
     * @param maps 想要拼接的键值对
     * @return 返回一个拼接好的字符串
     */
    public static String mapLinkSql(Map<String, Object> maps) {
        StringBuilder linkSql = new StringBuilder();
        int size = maps.size();
        int i = 0;
        for(String key:maps.keySet()){
            i++;
            if(i == size){
                linkSql.append(key);
                break;
            }
            linkSql.append(key+",");
        }
        return linkSql.toString();
    }


    /**
     * 使用反射将对象里面的变量拼接，格式为 xxx,xxx，主要是insert中的
     * 展示没用到
     * @param object 想要拼接的对象
     * @return 返回一个拼接好的字符串
     */
    public static String insertLinkSql(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        int size = fields.length;
        StringBuilder linkSql = new StringBuilder();
        int i = 0;
        for(Field field:fields){
            i++;
            if(i == size){
                linkSql.append(field.getName());
                break;
            }
            linkSql.append(field.getName()+",");
        }
        return linkSql.toString();
    }

    /**
     * 设置将所有Map集合中的数据以此放入list集合
     * @param list
     * @param map
     */
    public static void mapList(List list, Map<String, Object> map) { ;
        for(String key:map.keySet()){
            list.add(map.get(key));
        }
    }

    /**
     * 设置将一个pojo中的值放入list集合中
     * @param list
     * @param object
     */
    public static void pojoList(List list, Object object) throws IllegalAccessException {

        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            list.add(field.get(object));
        }

    }

    /**
     * 传入两个list集合将两个list集合连在一起
     * @param pre list集合里面部分
     * @param next list集合后面部分
     */
    public static void listAndList(List pre, List next) {

        for(int i = 0;i < next.size();i++){
            pre.add(next.get(i));
        }

    }

}
