package com.cooperation.record.utils;

/**
 * 给搜索的字段标红
 * @author cyl
 * @date 2021/11/21
 */
public class AddColor {

    /**
     * 传入一个标题，和一个要标红的字符
     */
    public static String addColorTitle (String novel, String search) {
        // 使用内容替换
        String midden = novel.replaceAll(search,"<font color='red'>"+search+"</font>");
        return midden;
    }

    /**
     * 传入文章内容，删除前面的，并标红
     * @param novel
     * @param search
     * @return
     */
    public static String addColorNovel (String novel, String search) {
        // 删除前面的内容
        novel = novel.substring(novel.indexOf(search));
        // 使用内容替换
        String midden = novel.replaceAll(search,"<font color='red'>"+search+"</font>");
        return midden;
    }

    public static void main(String[] args) {
        String test = addColorTitle("你好","好");
        String testOne = addColorNovel("你好","好");
    }

}
