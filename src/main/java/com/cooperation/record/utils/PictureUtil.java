package com.cooperation.record.utils;


import gui.ava.html.image.generator.HtmlImageGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 打印图片工具类
 * @author cyl
 * @date 2021/11/02
 */
public class PictureUtil {

    private JEditorPane editorPane;
    private static final Dimension DEFAULT_SIZE = new Dimension(800, 800);

    public static Map<String, String> types = new HashMap<String, String>();
    static
    {
        types.put("gif", "gif");
        types.put("jpg", "jpg");
        types.put("jpeg", "jpg");
        types.put("png", "png");
    }
    public static String formatForExtension(String extension)
    {
        String type = (String)types.get(extension);
        if (type == null){
            return "jpg";
        }

        return type;
    }

    public static String formatForFilename(String fileName) {
        int dotIndex = fileName.lastIndexOf(46);
        if (dotIndex < 0) {
            return "jpg";
        }
        String ext = fileName.substring(dotIndex + 1);
        return formatForExtension(ext);
    }


    public PictureUtil() {
        editorPane = createJEditorPane();
    }



    public void setSize(Dimension dimension) {
        editorPane.setPreferredSize(dimension);
    }

//    public void loadUrl(URL url) {
//        try {
//            editorPane.setPage(url);
//        } catch (IOException e) {
//            throw new RuntimeException(String.format("Exception while loading %s", url), e);
//        }
//    }

    public void loadUrl(String url) {
        try {
            editorPane.setPage(url);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Exception while loading %s", url), e);
        }
    }

    public void loadHtml(String html) {
        editorPane.setEditable(false);
        editorPane.setText(html);
        editorPane.setContentType("text/html");
        onDocumentLoad();
    }


    public void saveAsImage(String file) {
        saveAsImage(new File(file));
    }

    public void saveAsImage(File file) {
        BufferedImage image = getBufferedImage();

        BufferedImage bufferedImageToWrite = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImageToWrite.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

        final String formatName =  formatForFilename(file.getName());
        try {
            if (!ImageIO.write(bufferedImageToWrite, formatName, file)) {
                throw new IOException("No formatter for specified file type [" + formatName + "] available");
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Exception while saving '%s' image", file), e);
        }
    }

    protected void onDocumentLoad() {
    }

    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public BufferedImage getBufferedImage() {
        JFrame frame = new JFrame();
        frame.setPreferredSize(editorPane.getPreferredSize());
        frame.setUndecorated(true);
        frame.add(editorPane);
        frame.pack();

        Dimension prefSize = frame.getPreferredSize();
        BufferedImage img = new BufferedImage(prefSize.width, prefSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = img.getGraphics();

        frame.setVisible(true);
        frame.paint(graphics);
        frame.setVisible(false);
        frame.dispose();

        return img;
    }

    protected JEditorPane createJEditorPane() {
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setSize(getDefaultSize());
        editorPane.setEditable(false);
        EditorKit kit = new HTMLEditorKit();
        editorPane.setEditorKitForContentType("text/html", kit);
        editorPane.setContentType("text/html");

        return editorPane;
    }

//    public static void main(String[] args) {
//    	/*
//        HtmlImgGenerator imageGenerator = new HtmlImgGenerator();
//        imageGenerator.setSize(new Dimension(1000, 1000));
//        String url = new File("D:/test.html").toURI().toString();
//        imageGenerator.loadUrl(url);
//        //imageGenerator.loadHtml(String html);//加载本地html
//        imageGenerator.getBufferedImage();
//        imageGenerator.saveAsImage("D:/aaa.png");
//        */
//        String content="<div><div><p><font color=\"#454d66\">I want to know if you can be with joy, mine or your own, if you can dance with 11111</font></p><p><font color=\"#454d66\">wildness and let the ecstasy fill you to the tips of your fingers and toes without cautioning us to be careful, to be realistic,to remember the limitations of being human. </font><br><font color=\"#f9b4ab\">我想知道，你是否能安享快乐——我的或者你自己的，你是否能充满野性地舞蹈，让狂喜注满你的指尖和足尖，而不告诫我们要小心 、要现实、要记住人的存在的局限。 </font></p><p><font color=\"#ff6150\">It doesn't interest me if the story you are telling me is true. I want to know if you can disappoint another to be true to yourself; if you can bear the accusation of betrayal and not betray your own soul; if you can be faithless and therefore trust worthy. </font><br><font color=\"#679186\">我不关心，你告诉我的故事是否真实。我想知道，你是否能为了真实地对待自己而不怕别人失望；你是否能承受背叛的指责而不出卖自己的灵魂；我想知道，你是否能忠心耿耿从而值得信赖。 </font></p><p><font color=\"#264e70\">I want to know if you can see beauty even when it's not pretty,every day, and if you can source your own life from its presence. </font><br><font color=\"#58b368\">我想知道，你是否能保持精神饱满的状态，－－即使每天的生活并不舒心，你是否能从上帝的存在中寻求自己生命的来源。</font></p></div></div>";
//                /*html2Img(content,"D:/b.jpg");*/
//        html2Img("生活",content,"D:/aaa.png");
//    }

    public static String picture (String title,String content) {

        String path = UUID.randomUUID().toString();

        html2Img(title,content,"D:\\Record\\src\\main\\webapp\\picture\\"+path+".jpg");

        return "D:\\Record\\src\\main\\webapp\\picture\\"+path+".jpg";
    }

    /**
     *
     * @Description HTML转Image
     * @return 希望生成的Image Location
     */
    public static String html2Img(String title,String content, String saveImageLocation){

        StringBuffer buffer = new StringBuffer();

        buffer.append(" <html> ");
        buffer.append(" <head> ");
        buffer.append(" <title></title> ");
        buffer.append(" <style> ");
        buffer.append(" </style> ");
        buffer.append(" </head> ");
        buffer.append(" <body style=\" width: 800px;\"  > ");
        buffer.append(" 	<div class=\"main2\" style=\"margin-left: 50px;\"> ");
        buffer.append(" 			<table class=\"tab2\" width=\"100%\">	 ");
        buffer.append(" 			<tr> ");
        buffer.append(" 				<td class=\"td_bg2\"  style=\" width:10%;\"  align=\"right\">标题：</td> ");
        buffer.append(" 				<td> "+ title +" </td> ");
        buffer.append(" 			</tr> ");
        buffer.append(" 			<tr> ");
        buffer.append(" 				<td class=\"td_bg2\" align=\"right\"></td> ");
        buffer.append(" 				<td> ");
        buffer.append(" 					<div id=\"content\" name=\"content\" style=\"width:90%;height:450px; \"> ");
        buffer.append(" 						 #nr# ");
        buffer.append(" 					</div> ");
        buffer.append(" 				</td> ");
        buffer.append(" 			</tr> ");
        buffer.append(" 		</table> ");
        buffer.append(" 	</div> ");
        buffer.append(" </body> ");
        buffer.append(" </html> ");

        PictureUtil imageGenerator = new PictureUtil();
        try {
            String str=buffer.toString();

            str = str.replaceAll("#nr#",content);

            //str = str.toString().replaceAll("我方已接受","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我方已接受");
            //str = str.toString().replaceAll("特此通知","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;特此通知");
            //str = str.toString().replaceAll("style=\"float:right; clear:both;\">","style=\"float:right; clear:both;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

            str = str.toString().replaceAll("<span style=\"margin-left:30px\">","<span style=\"margin-left:30px\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            str = str.toString().replaceAll("<span style=\"margin-left:32px\">","<span style=\"margin-left:32px\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            str = str.toString().replaceAll("<style=\"float:right; clear:both;\">","<style=\"float:right; clear:both;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            str = str.toString().replaceAll("style=\"float:right; clear:both;\">","style=\"float:right; clear:both;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

            //str = str.toString().replaceAll("<br> ","<br> <br> ");

            imageGenerator.loadHtml(str);
            imageGenerator.getBufferedImage();
            imageGenerator.saveAsImage(saveImageLocation);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("将HTML文件转换成图片异常");
        }
        return saveImageLocation;
    }



    /**
     *
     * @Description HTML转Image
     * @param htmText HTML文本字符串
     * @return 希望生成的Image Location
     */
    public static String html2Img(String htmText, String saveImageLocation){

        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        try {
            imageGenerator.loadHtml(htmText);
            Thread.sleep(100);
            imageGenerator.getBufferedImage();
            Thread.sleep(200);
            imageGenerator.setSize(new Dimension(100,100));
            imageGenerator.saveAsImage(saveImageLocation);
            //imageGenerator.saveAsHtmlWithMap("hello-world.html", saveImageLocation);
            //不需要转换位图的，下面三行可以不要
            /*BufferedImage sourceImg = ImageIO.read(new File(saveImageLocation));
            sourceImg = transform_Gray24BitMap(sourceImg);
            ImageIO.write(sourceImg, "BMP", new File(saveImageLocation));*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("将HTML文件转换成图片异常");
        }
        return saveImageLocation;
    }

}
