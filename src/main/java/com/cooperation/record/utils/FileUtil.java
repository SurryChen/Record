package com.cooperation.record.utils;

import com.alibaba.fastjson.JSONObject;

import com.cooperation.record.common.enums.ApiInfo;
import com.cooperation.record.domain.vo.ApiMsg;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传与下载的工具类
 * @author cyl
 * @date 2021/10/27
 */
public class FileUtil {

    /**
     * 下载文件，并返回路径
     * @param request
     * @param real
     * @param fileTypeOther
     * @return
     * @throws Exception
     */
    public static ApiMsg load(HttpServletRequest request, String real, String fileTypeOther) throws Exception{

        ApiMsg apiMsg = new ApiMsg<List<String>>(ApiInfo.SUCCESS);

        /**
         * 返回的路径的list集合
         */
        List<String> list = new ArrayList<>();
        List<String> listReturn = new ArrayList<>();
        try {
            // 设置文件存储的位置
            String realPath = real;
            // 设置临时目录。
            // 上传文件大于缓冲区则先放于临时目录中
            String tempPath = "D:\\tempPath";
            // 判断存放上传文件的目录是否存在（不存在则创建）
            File file = new File(realPath);
            if(!file.exists()&&!file.isDirectory()){
                file.mkdir();
            }
            // 判断临时目录是否存在（不存在则创建）
            File fileTemp = new File(tempPath);
            if(!fileTemp.isDirectory()){
                fileTemp.mkdir();
            }
            /**
             * 使用Apache文件上传组件处理文件上传步骤
             */
            // 1、设置环境:创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置上传文件的临时目录
            factory.setRepository(fileTemp);
            // 2、核心操作类:创建一个文件上传解析器。
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 解决上传"文件名"的中文乱码
            upload.setHeaderEncoding("UTF-8");
            // 3、判断enctype:判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                System.out.println("不是上传文件，终止");
                return apiMsg = new ApiMsg(ApiInfo.WRONG);
            }
            //限制单个上传文件大小(1M)
            upload.setFileSizeMax(1024*1024);
            //限制总上传文件大小(5M)
            upload.setSizeMax(1024*1024*5);
            // 如果文件过大，会在这里报错
            // 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> items;
            try {
                items = upload.parseRequest(request);
            }catch (Exception e){
                return apiMsg = new ApiMsg<>(ApiInfo.File_OVERLOAD);
            }
            for(FileItem item:items){
                if(item.isFormField()){
                    // 不是文件的一律是错误
                    return apiMsg = new ApiMsg(ApiInfo.WRONG);
                }else{
                    String fileName = item.getName();
                    // 多个文件上传输入框有空的异常处理
                    // 去空格是否为空
                    if(fileName==null||"".equals(fileName.trim())){
                        // 为空，跳过当次循环，  第一个没输入则跳过可以继续输入第二个
                        continue;
                    }
                    // 获取文件名
                    fileName = UUID.randomUUID().toString()+"_"+fileName.substring(fileName.lastIndexOf("\\")+1);
                    // 拼接上传路径。存放路径+上传的文件名
                    String filePath = realPath+"\\"+fileName;
                    // 构建输入输出流
                    InputStream in = item.getInputStream(); //获取item中的上传文件的输入流
                    OutputStream out = new FileOutputStream(filePath); //创建一个文件输出流
                    // 创建一个缓冲区，缓冲区一般是10k，暂时保存信息
                    byte b[] = new byte[1024];
                    // 判断输入流中的数据是否已经读完的标识
                    int len = -1;
                    // 循环将输入流读入到缓冲区当中，(len=in.read(buffer))！=-1就表示in里面还有数据
                    while((len=in.read(b)) != -1){  //没数据了返回-1
                        // 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath+"\\"+filename)当中
                        out.write(b, 0, len);
                    }
                    //关闭流
                    out.close();
                    in.close();
                    //删除临时文件
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 删除处理文件上传时生成的临时文件
                    item.delete();
                    // 添加文件路径
                    list.add(filePath);
                    System.out.println("-----------------");
                    System.out.println(filePath);
                    System.out.println("-----------------");
                    /**
                     * 要经过两次转义，还有一次是正则
                     */
                    listReturn.add("http://localhost:8080/"+filePath.substring(26).replaceAll("\\\\","/"));
                    // 判断是否需要删除
                    File delete = new File(filePath);
                    String fileType = filePath.substring(filePath.lastIndexOf("."),filePath.length());
                    // 判断是什么文件，如果不是图片文件
                    if("picture".equals(fileTypeOther)){
                        if(!".jpg".equals(fileType) && !".png".equals(fileType)){
                            // 删除文件
                            delete.delete();
                            // 删除之前下载的文件
                            for(int i = 0;i < list.size();i++){
                                File deleteOther = new File(filePath);
                                deleteOther.delete();
                            }
                            return apiMsg = new ApiMsg<>(ApiInfo.TYPE_ERROR);
                        }
                    } else if ("table".equals(fileTypeOther)) {
                        /**
                         * 不管格式正不正确都需要删除
                         */
                        if(!".xlsx".equals(fileType) && !".xls".equals(fileType)){
                            // 删除文件
                            delete.delete();
                            // 删除之前下载的文件
                            for(int i = 0;i < list.size();i++){
                                File deleteOther = new File(filePath);
                                deleteOther.delete();
                            }
                            return apiMsg = new ApiMsg<>(ApiInfo.TYPE_ERROR);
                        } else {
                            // 文件类型正确，所以就要判断格式
                            // 传递文件路径交由其他方法判断
                            JSONObject jsonObject = tableFile(list);
                            if(jsonObject != null) {
                                apiMsg = new ApiMsg<>(ApiInfo.SUCCESS);
                                apiMsg.setData(jsonObject);
                                // 删除文件
                                delete.delete();
                                // 删除之前下载的文件
                                for(int i = 0;i < list.size();i++){
                                    File deleteOther = new File(filePath);
                                    deleteOther.delete();
                                }
                                return apiMsg;
                            } else {
                                // 删除文件
                                delete.delete();
                                // 删除之前下载的文件
                                for(int i = 0;i < list.size();i++){
                                    File deleteOther = new File(filePath);
                                    deleteOther.delete();
                                }
                                return apiMsg = new ApiMsg<>(ApiInfo.TYPE_ERROR);
                            }
                        }
                    } else if ("word".equals(fileTypeOther)) {
                        // 判断格式是否正确
                        if(!".docx".equals(fileType) && !".DOCX".equals(fileType)){
                            // 删除文件
                            delete.delete();
                            // 删除之前下载的文件
                            for(int i = 0;i < list.size();i++){
                                File deleteOther = new File(filePath);
                                deleteOther.delete();
                            }
                            return apiMsg = new ApiMsg<>(ApiInfo.TYPE_ERROR);
                        }
                        /**
                         * 返回服务器路径
                         */
                        apiMsg.setData(list);
                        return apiMsg;
                    }
                }
            }
            apiMsg.setData(listReturn);
            return apiMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return apiMsg = new ApiMsg(ApiInfo.WRONG);
        }
    }

    public static JSONObject tableFile(List<String> list) throws IOException {

        try {
            for (int j = 0; j < list.size(); j++) {
                String path = list.get(j);
                // 创建文件输入流
                FileInputStream fileInput = null;
                /**
                 * .xls文件手动修改后缀为.xlsx会报错
                 */
                /**
                 * 有两种工作簿
                 * 格式一旦不对就会报错
                 * 抓一下异常，报错就换一种格式
                 */
                Workbook workbook = null;
                try {
                    fileInput = new FileInputStream(path);
                    workbook = new XSSFWorkbook(fileInput);
                }catch (Exception e){
                    fileInput = new FileInputStream(path);
                    workbook = new HSSFWorkbook(fileInput);
                }
                // 获取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                /**
                 * 因为设置的时候就是按照列来编辑进入JSON格式数据
                 * 所以要分列
                 */
                // 获取表格内容的最后一行的行数
                int lastRowNum = sheet.getLastRowNum();
                // 获取最大列数
                Row rowOther = sheet.getRow(0);
                int col = rowOther.getLastCellNum();
                // 不是有七天的不行
                if (col != 8) {
                    return null;
                }
                // 标记一下第一行
                Row rowThree = sheet.getRow(0);
                // 返回的最外层的JSON格式数据
                JSONObject jsonObject = new JSONObject();
                // 设置时间格式
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                // 使用列遍历
                // 从Excel中的第二行开始，也就是这里的1
                for (int i = 1; i < col; i++) {
                    // 每一天的键值
                    List<JSONObject> listJson = new ArrayList<>();
                    // 使用行遍历
                    for (int h = 1; h < lastRowNum; h = h + 4) {
                        JSONObject jsonObjectOne = new JSONObject();
                        // 从1开始，单数是时间，双数是内容，0号位是周几
                        // i是列
                        Row rowOne = sheet.getRow(h);
                        Row rowTwo = sheet.getRow(h + 1);
                        Row rowFour = sheet.getRow(h + 2);
                        // 导入的计划默认为未完成
                        Cell cellOne = rowOne.getCell(i);
                        Cell cellTwo = rowTwo.getCell(i);
                        Cell cellFour = rowFour.getCell(i);
                        /**
                         * 每获取到一个单元格，都需要判断是否为空
                         */
                        if(isEmptyCell(cellOne) || isEmptyCell(cellTwo) || isEmptyCell(cellFour)){
                            continue;
                        }
                        /**
                         * 不为空，开始获取值
                         */
                        // 获取单元格的时间格式值
                        // 如果不是不符合格式，直接结束该行，不再循环
                        String cellValue = null;
                        String cellEndTime = null;
                        try {
                            // 将数字转成日期格式的方法
                            Date date = cellOne.getDateCellValue();
                            Date endDate = cellTwo.getDateCellValue();
                            // 保存日期
                            cellValue = format.format(date);
                            cellEndTime = format.format(endDate);
                        }catch (Exception e){
                            continue;
                        }
                        /**
                         * 加入数据
                         */
                        jsonObjectOne.put("startTime",cellValue);
                        jsonObjectOne.put("endTime",cellEndTime);
                        // 不能直接加入单元格啊
                        jsonObjectOne.put("content",cellFour.toString());
                        jsonObjectOne.put("condition","否");
                        listJson.add(jsonObjectOne);
                    }
                    jsonObject.put(rowThree.getCell(i).getStringCellValue(),listJson);
                }
                workbook.close();
                fileInput.close();
                return jsonObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断单个单元格是否为空
     */
    public static boolean isEmptyCell(Cell cell) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }
        return false;
    }
}
