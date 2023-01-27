package com.cooperation.record.utils;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMailTextPictureEnclosure {
    //发件人地址
    public static String senderAddress = "1974023122@qq.com";
    //收件人地址
    public static String recipientAddress = "1974023122@qq.com";
    //发件人账户名
    public static String senderAccount = "1974023122@qq.com";
    //发件人账户密码
    public static String senderPassword = "";

    public static void main(String[] args) throws Exception {
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg = getMimeMessage(session);
        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());

        //5、关闭邮件连接
        transport.close();
    }

    /**
     * 获得创建一封邮件的实例对象
     * @param session
     * @return
     * @throws MessagingException
     * @throws AddressException
     */
    public static MimeMessage getMimeMessage(Session session) throws Exception{
        //1.创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //2.设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddress));
        //4.设置邮件主题
        msg.setSubject("邮件主题(包含图片和附件)","UTF-8");

        //下面是设置邮件正文
        //msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");

        // 5. 创建图片"节点"
        MimeBodyPart image = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh = new DataHandler(new FileDataSource("D:\\Record\\src\\main\\webapp\\picture\\pi.png"));
        // 将图片数据添加到"节点"
        image.setDataHandler(dh);
        // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
        image.setContentID("mailTestPic");

        /*String attachmentFiles[] = {.......};
        for(int i = 0; i < attachmentFiles.length; i++) {
            MimeBodyPart mb = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(attachmentFiles[i]);
            mb.setDataHandler(new DataHandler(fds));
            mb.setFileName((new File(attachmentFiles[i])).getName());
            mpart.addBodyPart(mb);
        }*/

        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent("<html>\n" +
                "    <head>\n" +
                "        <title>手机接收</title>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=0.5,\n" +
                "        maximum-scale=2.0, user-scalable=no\" charset=\"UTF-8\"/>\n" +
                "         <style type=\"text/css\">\n" +
                "                body{\n" +
                "                    /* min-width:1260px; */\n" +
                "                    margin: 0 auto;\n" +
                "                    padding: 0;\n" +
                "                }\n" +
                "                .wrap{\n" +
                "                    /* width: 360px;\n" +
                "                    height: 761px; */\n" +
                "                    width: 100%;\n" +
                "                    height: 860px;\n" +
                "                    margin: 0 auto;\n" +
                "                    background-color: #FFF;\n" +
                "                }\n" +
                "                .wrap-inner{\n" +
                "                    width: 96%;\n" +
                "                    height: 98%;\n" +
                "                    margin:20px  auto;\n" +
                "                    border: 1px solid #979595;\n" +
                "                    border-radius: 2%;\n" +
                "                }\n" +
                "                .wrap-pic{\n" +
                "                    /* width: 320px;\n" +
                "                    height: 320px; */\n" +
                "                    width: 94%;\n" +
                "                    height: 28%;\n" +
                "                    margin:20px auto 0;\n" +
                "                    border-radius: 10px;\n" +
                "                    background-color: #5F5F5F;\n" +
                "                }\n" +
                "                .wrap-pic img{\n" +
                "                    width: 100%;\n" +
                "                    height: 100%;\n" +
                "                    /* border: none; */\n" +
                "                    border-radius: 10px;\n" +
                "                }\n" +
                "                .resume{\n" +
                "                    /* width: 320px;\n" +
                "                    height: 230px; */\n" +
                "                    /* position: relative; */\n" +
                "                    /* width: 90%; */\n" +
                "                    height: 26%;\n" +
                "                    margin: -50px auto 0;\n" +
                "                    text-align: center;\n" +
                "                }\n" +
                "                .resume-top{\n" +
                "                    /* position: absolute;\n" +
                "                    top: -50px;\n" +
                "                    left: 48%; */\n" +
                "                    width: inherit;\n" +
                "                    /* height: 100px; */\n" +
                "                    /* height: 44%; */\n" +
                "                    z-index: 1;\n" +
                "                    /* margin:20px 0; */\n" +
                "                }\n" +
                "                .resume-circle{\n" +
                "                    display: inline-block;\n" +
                "                    width: 150px;\n" +
                "                    height: 150px;\n" +
                "                    /* width: 31%;\n" +
                "                    height: 100%; */\n" +
                "                    border-radius: 50%;\n" +
                "                    background-color: #233560 ;\n" +
                "                    /* vertical-align: middle; */\n" +
                "                }\n" +
                "                .resume-circle img{\n" +
                "                    width: inherit;\n" +
                "                    height: inherit;\n" +
                "                    border-radius: 50%;\n" +
                "                }\n" +
                "                .resume-name{\n" +
                "                    /* display: inline-block; */\n" +
                "                    /* width: 90px;\n" +
                "                    height: 30px; */\n" +
                "                    /* width: 90px; */\n" +
                "                    /* height: 100%; */\n" +
                "                    height: 13%;\n" +
                "                    margin: 5px auto 0;\n" +
                "                    /* margin-left: 20px; */\n" +
                "                    /* margin-left: 2%; */\n" +
                "                    color: #333;\n" +
                "                    font-size: 19px;\n" +
                "                    font-weight: 400;\n" +
                "                    text-align: center;\n" +
                "                    /* line-height: 100%; */\n" +
                "                    /* vertical-align: middle; */\n" +
                "                }\n" +
                "                .resume-middle{\n" +
                "                    /* width: inherit; */\n" +
                "                    /* height: 25px; */\n" +
                "                    /* height: 20%; */\n" +
                "                    height: 12%;\n" +
                "                    /* margin: 10px auto 0; */\n" +
                "                    color: #333;\n" +
                "                    font-size: 13px;\n" +
                "                    line-height: 25px;\n" +
                "                    text-align: center;\n" +
                "                }\n" +
                "                .planing{\n" +
                "                    width: 94%;\n" +
                "                    margin: 0 auto;\n" +
                "                }\n" +
                "                .planing-title{\n" +
                "                    height: 70px;\n" +
                "                    margin: 0 auto;\n" +
                "                    background: url(cid:plan.png) no-repeat 51% 84%;\n" +
                "                    background-size: 209px 11px;\n" +
                "                    text-align: center;\n" +
                "                }\n" +
                "                .planing-title img{\n" +
                "                    width: 49px;\n" +
                "                    height: 46px;\n" +
                "                    vertical-align: middle;\n" +
                "                }\n" +
                "                .planing-title span{\n" +
                "                    margin-left: 10px;\n" +
                "                    color: #333;\n" +
                "                    font-size: 15px;\n" +
                "                    font-weight: 400;\n" +
                "                    opacity: .98;\n" +
                "                    vertical-align: middle;\n" +
                "                }\n" +
                "                .planing p{\n" +
                "                    color: #333;\n" +
                "                    font-size: 13px;\n" +
                "                    font-weight: 400;\n" +
                "                    opacity: .98;\n" +
                "                }\n" +
                "                .plan-content{\n" +
                "                    width: 100%;\n" +
                "                }\n" +
                "                table{\n" +
                "                    color: #333;\n" +
                "                    font-size: 16px;\n" +
                "                    font-weight: 400;\n" +
                "                    opacity: .98;\n" +
                "                }\n" +
                "                tr{\n" +
                "                    height: 40px;\n" +
                "                    background: url(\"cid:Wavyline.png\") no-repeat 26px 100%;\n" +
                "                    background-size: contain;\n" +
                "                    \n" +
                "                }\n" +
                "                table tr td{\n" +
                "                    padding: 0 5px;\n" +
                "                }\n" +
                "\n" +
                "         </style>\n" +
                "        <!-- <link href=\"css/phone.css\" rel=\"stylesheet\"  type=\"text/css\"> -->\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"wrap\">\n" +
                "            <div class=\"wrap-inner\">\n" +
                "                <div class=\"wrap-pic\">\n" +
                "                    <img src=\"cid:sun.png\">\n" +
                "                </div>\n" +
                "                <div class=\"resume\">\n" +
                "                    <div class=\"resume-top\">\n" +
                "                        <div class=\"resume-circle\">\n" +
                "                            <img src=\"cid:sun.png\">\n" +
                "                        </div>\n" +
                "                        <div class=\"resume-name\">名字</div>\n" +
                "                        <div class=\"resume-middle\">\n" +
                "                            \"好好学习，天天向上\"\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "               <div class=\"planing\">\n" +
                "                   <div class=\"planing-title\">\n" +
                "                       <img src=\"cid:sun.png\">\n" +
                "                       <span>今日计划清单</span>\n" +
                "                    </div>\n" +
                "                    <p>2021/10/22</p>\n" +
                "                    <p>Thur.</p>\n" +
                "                   <table >\n" +
                "                       <tr>\n" +
                "                           <td>1.</td>\n" +
                "                           <td>5:00~6:30</td>\n" +
                "                           <td>背单词</td>\n" +
                "                       </tr>\n" +
                "                       <tr>\n" +
                "                            <td>2.</td>\n" +
                "                            <td>7:00~10:00</td>\n" +
                "                            <td>睡觉</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>3.</td>\n" +
                "                            <td>7:00~10:00</td>\n" +
                "                            <td>睡觉</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>4.</td>\n" +
                "                            <td>7:00~10:00</td>\n" +
                "                            <td>睡觉</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>5.</td>\n" +
                "                            <td>7:00~10:00</td>\n" +
                "                            <td>睡觉</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>6.</td>\n" +
                "                            <td>7:00~10:00</td>\n" +
                "                            <td>睡觉</td>\n" +
                "                        </tr>\n" +
                "                   </table>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>", "text/html;charset=UTF-8");

        // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
        MimeMultipart mm_text_image = new MimeMultipart();
        // 添加图片
        mm_text_image.addBodyPart(text);
        String attachmentFiles[] = {"D:\\Record\\src\\main\\webapp\\picture\\important\\leave.png",
                "D:\\Record\\src\\main\\webapp\\picture\\important\\plan.png",
                "D:\\Record\\src\\main\\webapp\\picture\\important\\sun.png",
                "D:\\Record\\src\\main\\webapp\\picture\\important\\Wavyline.png"};
        for(int i = 0; i < attachmentFiles.length; i++) {

            // 5. 创建图片"节点"
            MimeBodyPart imageOther = new MimeBodyPart();
            // 读取本地文件
            DataHandler dhOther = new DataHandler(new FileDataSource(attachmentFiles[i]));
            // 将图片数据添加到"节点"
            imageOther.setDataHandler(dhOther);
            // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
            imageOther.setContentID((new File(attachmentFiles[i])).getName());
            mm_text_image.addBodyPart(imageOther);

        }
//        mm_text_image.addBodyPart(image);
        mm_text_image.setSubType("related");    // 关联关系

        // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        // 上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
        MimeBodyPart text_image = new MimeBodyPart();
        text_image.setContent(mm_text_image);

        // 9. 创建附件"节点"
        MimeBodyPart attachment = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh2 = new DataHandler(new FileDataSource("src\\mailTestDoc.docx"));
        // 将附件数据添加到"节点"
        attachment.setDataHandler(dh2);
        // 设置附件的文件名（需要编码）
        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        MimeMultipart mm = new MimeMultipart();


        mm.addBodyPart(text_image);
//        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
        mm.setSubType("mixed");         // 混合关系

        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        msg.setContent(mm);
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }

}
