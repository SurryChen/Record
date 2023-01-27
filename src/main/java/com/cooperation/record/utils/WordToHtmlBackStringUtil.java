package com.cooperation.record.utils;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.converter.core.*;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class WordToHtmlBackStringUtil {

    public static String convert(String filePath) throws Exception {

        //获取文件名称
        File file = new File(filePath);
        String fileName = file.getName();

        //获取文件所在目录
        if (filePath.lastIndexOf("/") > 0){
            filePath = filePath.substring(0,filePath.lastIndexOf("/") + 1);
        }else if (filePath.lastIndexOf("\\") > 0){
            filePath = filePath.substring(0,filePath.lastIndexOf("\\") + 1);
        }

        //获取文件html名称
        String htmlName = fileName.substring(0, fileName.lastIndexOf(".")) + ".html";
        if (fileName.endsWith(".docx") || fileName.endsWith(".DOCX")) {
            try {
                docx(filePath, fileName, htmlName);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            return null;
            /*doc(filePath, fileName, htmlName);*/
        }

        // 删除word文档
        file.delete();

        String readFile = readFile(filePath + htmlName);
        return readFile;
    }


    public static void docx(String filePath ,String fileName,String htmlName) throws Exception{

        final String filePathName = filePath + fileName;
        System.out.println("文件路径"+filePath);
        InputStream in = new FileInputStream(new File(filePathName));
        // 1.加载解析docx文档用的XWPFDocument对象
        XWPFDocument document = new XWPFDocument(in);
        // 2.解析XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
        File imageFolderFile = new File(filePath);//new FileURIResolver(imageFolderFile)

        // 将UUID保存一下，以供后面使用，顺序应该是后一个先执行
        List<String> list = new ArrayList<>();
        List listOther = new ArrayList();
        listOther.add(0);

        XHTMLOptions options = XHTMLOptions.create().URIResolver(new IURIResolver() {
            // 这里是图片的路径返回设置
            @Override
            public String resolve(String s) {
                // 获取到是第几个
                int i = (int)listOther.get(0);
                String imageName = s.substring(0,10)+"/"+ list.get(i) +".jpeg";
                System.out.println("imageName:"+imageName);
                // 清除
                listOther.clear();
                i++;
                // 标记是第几个了
                listOther.add(i);
                System.out.println("是第几"+list.size());
                return "http://localhost:8080/"+imageName;
            }
        });

        /* new FileImageExtractor(imageFolderFile)*/
        options.setExtractor(new IImageExtractor() {
            /**
             * 该方法是通过文件路径书写文件名，写入图片文件的接口
             * @param imagePath
             * @param imageData
             * @throws IOException
             */
            @Override
            public void extract(String imagePath, byte[] imageData) throws IOException {

                String id = UUID.randomUUID().toString();

                System.out.println("图片文件名："+imagePath.substring(5,11)+ id +".jpeg");
                list.add(id);
                File imageFile = new File(imageFolderFile, imagePath.substring(5,11)+ id +".jpeg");

                imageFile.getParentFile().mkdirs();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    in = new ByteArrayInputStream(imageData);
                    out = new FileOutputStream(imageFile);
                    IOUtils.copy(in, out);
                } finally {
                    if (in != null) {
                        IOUtils.closeQuietly(in);
                    }

                    if (out != null) {
                        IOUtils.closeQuietly(out);
                    }
                }
            }
        });

        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);
        // 3.将 XWPFDocument转换成XHTML
        OutputStream out = new FileOutputStream(new File(filePath + htmlName));
        XHTMLConverter.getInstance().convert(document, out, options);

    }

    public static String readFile(String filePath) {
        System.out.println("要删除的文件"+filePath);
        File file = new File(filePath);
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        byte[] bytes = new byte[1024];
        try {
            for (int i; (i = input.read(bytes)) != -1;) {
                buffer.append(new String(bytes, 0, i, "utf8"));
            }
            //流记得关闭，不然后面file不能被正常删除
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //转换成功后的html文件内容读取后删掉，防止不必要的占用服务器资源
        file.delete();
        return buffer.toString();
    }

}


