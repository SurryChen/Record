package com.cooperation.record.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * 发邮件的工具类型
 * @author cyl
 * @date 2021/10/10
 */
public class MailUtil {

    /**
     * 先设置发件人的邮箱账号和密码
     * 不能设置收件人信息和邮件信息，因为设置了可能会因为线程问题导致错误
     */
    private static final String USER = "1974023122@qq.com";
    private static final String PASSWORD = "wlsybyucrglhbdac";

    /**
     * 写一个发送邮箱的方法
     * @param addressee 收件人
     * @param text 邮件内容
     * @param title 邮件标题
     */
    public static void sendMail(String addressee,String text,String title) throws Exception {
        // 1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        /**
         * 服务器地址应该是可变的
         */
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        //2、通过授权信息和配置文件创建定义整个应用程序所需的环境信息的 Session 对象
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty(USER);
                String password = props.getProperty(PASSWORD);
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(props,authenticator);
        //设置调试信息在控制台打印出来
        /*session.setDebug(true);*/
        //3、创建邮件的实例对象
        Message msg = getMimeMessage(session,addressee,text,title);
        //4、根据session对象获取邮件传输对象Transport
        /**
         * getTransport()会报NoSuchProviderException异常，大致意思是信息不全
         */
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(USER, PASSWORD);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());
        //5、关闭邮件连接
        transport.close();
    }


    /**
     * 写一个发送邮箱的方法
     * @param addressee 收件人
     */
    public static void sendMail(String addressee) throws Exception {
        // 1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        /**
         * 服务器地址应该是可变的
         */
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        //2、通过授权信息和配置文件创建定义整个应用程序所需的环境信息的 Session 对象
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty(USER);
                String password = props.getProperty(PASSWORD);
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(props,authenticator);
        //设置调试信息在控制台打印出来
        /*session.setDebug(true);*/
        //3、创建邮件的实例对象
        Message msg = getMimeMessage(session,addressee);
        //4、根据session对象获取邮件传输对象Transport
        /**
         * getTransport()会报NoSuchProviderException异常，大致意思是信息不全
         */
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(USER, PASSWORD);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());
        //5、关闭邮件连接
        transport.close();
    }





    /**
     * 获得创建一封邮件的实例对象
     * @param session
     * @return
     */
    public static MimeMessage getMimeMessage(Session session,String addressee,String text,String title) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(USER));
        /*//**//**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(addressee));
        //设置邮件主题
        msg.setSubject(title,"UTF-8");
        //设置邮件正文
        msg.setContent(text, "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        return msg;
    }

    /**
     * 发送图片方法，每日计划的邮箱部分
     * @param
     * @throws Exception
     */
    public static MimeMessage getMimeMessage(Session session,String addressee) throws Exception{
        //1.创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //2.设置发件人地址
        msg.setFrom(new InternetAddress(addressee));
        /**
         * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(addressee));
        //设置邮件主题
        msg.setSubject("今日计划","UTF-8");
        //设置邮件正文

        // 5. 创建图片"节点"
        MimeBodyPart image = new MimeBodyPart();
        // 读取本地文件
        DataHandler dh = new DataHandler(new FileDataSource("D:\\Record\\src\\main\\webapp\\picture\\pi.png"));
        // 将图片数据添加到"节点"
        image.setDataHandler(dh);
        // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
        image.setContentID("1");

        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        text.setContent("<html>\\n\" +\n" +
                "                \"    <head>\\n\" +\n" +
                "                \"        <title>手机接收</title>\\n\" +\n" +
                "                \"        <meta name=\\\"viewport\\\" content=\\\"width=device-width,initial-scale=1.0, minimum-scale=0.5,\\n\" +\n" +
                "                \"        maximum-scale=2.0, user-scalable=no\\\" charset=\\\"UTF-8\\\"/>\\n\" +\n" +
                "                \"         <style type=\\\"text/css\\\">\\n\" +\n" +
                "                \"                body{\\n\" +\n" +
                "                \"                    /* min-width:1260px; */\\n\" +\n" +
                "                \"                    margin: 0 auto;\\n\" +\n" +
                "                \"                    padding: 0;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .wrap{\\n\" +\n" +
                "                \"                    /* width: 360px;\\n\" +\n" +
                "                \"                    height: 761px; */\\n\" +\n" +
                "                \"                    width: 100%;\\n\" +\n" +
                "                \"                    height: 1500px;\\n\" +\n" +
                "                \"                    margin: 0 auto;\\n\" +\n" +
                "                \"                    background-color: #FFF;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .wrap-inner{\\n\" +\n" +
                "                \"                    width: 96%;\\n\" +\n" +
                "                \"                    height: 98%;\\n\" +\n" +
                "                \"                    margin:20px  auto;\\n\" +\n" +
                "                \"                    border: 1px solid #979595;\\n\" +\n" +
                "                \"                    border-radius: 2%;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .wrap-pic{\\n\" +\n" +
                "                \"                    /* width: 320px;\\n\" +\n" +
                "                \"                    height: 320px; */\\n\" +\n" +
                "                \"                    width: 94%;\\n\" +\n" +
                "                \"                    height: 35%;\\n\" +\n" +
                "                \"                    margin:20px auto 0;\\n\" +\n" +
                "                \"                    border-radius: 10px;\\n\" +\n" +
                "                \"                    background-color: #5F5F5F;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .wrap-pic img{\\n\" +\n" +
                "                \"                    width: 100%;\\n\" +\n" +
                "                \"                    height: 100%;\\n\" +\n" +
                "                \"                    /* border: none; */\\n\" +\n" +
                "                \"                    border-radius: 10px;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .resume{\\n\" +\n" +
                "                \"                    /* width: 320px;\\n\" +\n" +
                "                \"                    height: 230px; */\\n\" +\n" +
                "                \"                    /* position: relative; */\\n\" +\n" +
                "                \"                    /* width: 90%; */\\n\" +\n" +
                "                \"                    height: 20%;\\n\" +\n" +
                "                \"                    margin: -50px auto 0;\\n\" +\n" +
                "                \"                    text-align: center;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .resume-top{\\n\" +\n" +
                "                \"                    /* position: absolute;\\n\" +\n" +
                "                \"                    top: -50px;\\n\" +\n" +
                "                \"                    left: 48%; */\\n\" +\n" +
                "                \"                    width: inherit;\\n\" +\n" +
                "                \"                    /* height: 100px; */\\n\" +\n" +
                "                \"                    /* height: 44%; */\\n\" +\n" +
                "                \"                    z-index: 1;\\n\" +\n" +
                "                \"                    /* margin:20px 0; */\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .resume-circle{\\n\" +\n" +
                "                \"                    display: inline-block;\\n\" +\n" +
                "                \"                    width: 200px;\\n\" +\n" +
                "                \"                    height: 200px;\\n\" +\n" +
                "                \"                    /* width: 31%;\\n\" +\n" +
                "                \"                    height: 100%; */\\n\" +\n" +
                "                \"                    border-radius: 50%;\\n\" +\n" +
                "                \"                    background-color: #233560 ;\\n\" +\n" +
                "                \"                    /* vertical-align: middle; */\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .resume-circle img{\\n\" +\n" +
                "                \"                    width: inherit;\\n\" +\n" +
                "                \"                    height: inherit;\\n\" +\n" +
                "                \"                    border-radius: 50%;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .resume-name{\\n\" +\n" +
                "                \"                    /* display: inline-block; */\\n\" +\n" +
                "                \"                    /* width: 90px;\\n\" +\n" +
                "                \"                    height: 30px; */\\n\" +\n" +
                "                \"                    /* width: 90px; */\\n\" +\n" +
                "                \"                    /* height: 100%; */\\n\" +\n" +
                "                \"                    height: 12%;\\n\" +\n" +
                "                \"                    margin: 5px auto 0;\\n\" +\n" +
                "                \"                    /* margin-left: 20px; */\\n\" +\n" +
                "                \"                    /* margin-left: 2%; */\\n\" +\n" +
                "                \"                    color: #333;\\n\" +\n" +
                "                \"                    font-size: 19px;\\n\" +\n" +
                "                \"                    font-weight: 400;\\n\" +\n" +
                "                \"                    text-align: center;\\n\" +\n" +
                "                \"                    /* line-height: 100%; */\\n\" +\n" +
                "                \"                    /* vertical-align: middle; */\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .resume-middle{\\n\" +\n" +
                "                \"                    /* width: inherit; */\\n\" +\n" +
                "                \"                    /* height: 25px; */\\n\" +\n" +
                "                \"                    /* height: 20%; */\\n\" +\n" +
                "                \"                    height: 12%;\\n\" +\n" +
                "                \"                    /* margin: 10px auto 0; */\\n\" +\n" +
                "                \"                    color: #333;\\n\" +\n" +
                "                \"                    font-size: 13px;\\n\" +\n" +
                "                \"                    line-height: 25px;\\n\" +\n" +
                "                \"                    text-align: center;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .planing{\\n\" +\n" +
                "                \"                    width: 94%;\\n\" +\n" +
                "                \"                    margin: 0 auto;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .planing-title{\\n\" +\n" +
                "                \"                    height: 70px;\\n\" +\n" +
                "                \"                    margin: 0 auto;\\n\" +\n" +
                "                \"                    background: url(images/plan.png) no-repeat 51% 84%;\\n\" +\n" +
                "                \"                    background-size: 209px 11px;\\n\" +\n" +
                "                \"                    text-align: center;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .planing-title img{\\n\" +\n" +
                "                \"                    width: 49px;\\n\" +\n" +
                "                \"                    height: 46px;\\n\" +\n" +
                "                \"                    vertical-align: middle;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .planing-title span{\\n\" +\n" +
                "                \"                    margin-left: 10px;\\n\" +\n" +
                "                \"                    color: #333;\\n\" +\n" +
                "                \"                    font-size: 15px;\\n\" +\n" +
                "                \"                    font-weight: 400;\\n\" +\n" +
                "                \"                    opacity: .98;\\n\" +\n" +
                "                \"                    vertical-align: middle;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .planing p{\\n\" +\n" +
                "                \"                    color: #333;\\n\" +\n" +
                "                \"                    font-size: 17px;\\n\" +\n" +
                "                \"                    font-weight: 400;\\n\" +\n" +
                "                \"                    opacity: .98;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                .plan-content{\\n\" +\n" +
                "                \"                    width: 100%;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                table{\\n\" +\n" +
                "                \"                    color: #333;\\n\" +\n" +
                "                \"                    font-size: 16px;\\n\" +\n" +
                "                \"                    font-weight: 400;\\n\" +\n" +
                "                \"                    opacity: .98;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                tr{\\n\" +\n" +
                "                \"                    height: 40px;\\n\" +\n" +
                "                \"                    background: url(images/Wavyline.png) no-repeat 26px 100%;\\n\" +\n" +
                "                \"                    background-size: contain;\\n\" +\n" +
                "                \"                    \\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"                table tr td{\\n\" +\n" +
                "                \"                    padding: 0 5px;\\n\" +\n" +
                "                \"                }\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"         </style>\\n\" +\n" +
                "                \"        <!-- <link href=\\\"css/phone.css\\\" rel=\\\"stylesheet\\\"  type=\\\"text/css\\\"> -->\\n\" +\n" +
                "                \"    </head>\\n\" +\n" +
                "                \"    <body>\\n\" +\n" +
                "                \"        <div class=\\\"wrap\\\">\\n\" +\n" +
                "                \"            <div class=\\\"wrap-inner\\\">\\n\" +\n" +
                "                \"                <div class=\\\"wrap-pic\\\">\\n\" +\n" +
                "                \"                    <img>\\n\" +\n" +
                "                \"                </div>\\n\" +\n" +
                "                \"                <div class=\\\"resume\\\">\\n\" +\n" +
                "                \"                    <div class=\\\"resume-top\\\">\\n\" +\n" +
                "                \"                        <div class=\\\"resume-circle\\\">\\n\" +\n" +
                "                \"                            <img>\\n\" +\n" +
                "                \"                        </div>\\n\" +\n" +
                "                \"                        <div class=\\\"resume-name\\\">名字</div>\\n\" +\n" +
                "                \"                        <div class=\\\"resume-middle\\\">\\n\" +\n" +
                "                \"                            \\\"好好学习，天天向上\\\"\\n\" +\n" +
                "                \"                        </div>\\n\" +\n" +
                "                \"                    </div>\\n\" +\n" +
                "                \"                </div>\\n\" +\n" +
                "                \"               <div class=\\\"planing\\\">\\n\" +\n" +
                "                \"                   <div class=\\\"planing-title\\\">\\n\" +\n" +
                "                \"                       <img src=\\\"cid:1\\\">\\n\" +\n" +
                "                \"                       <span>今日计划清单</span>\\n\" +\n" +
                "                \"                    </div>\\n\" +\n" +
                "                \"                    <p>2021/10/22</p>\\n\" +\n" +
                "                \"                    <p>Thur.</p>\\n\" +\n" +
                "                \"                   <table >\\n\" +\n" +
                "                \"                       <tr>\\n\" +\n" +
                "                \"                           <td>1.</td>\\n\" +\n" +
                "                \"                           <td>5:00~6:30</td>\\n\" +\n" +
                "                \"                           <td>背单词</td>\\n\" +\n" +
                "                \"                       </tr>\\n\" +\n" +
                "                \"                       <tr>\\n\" +\n" +
                "                \"                            <td>2.</td>\\n\" +\n" +
                "                \"                            <td>7:00~10:00</td>\\n\" +\n" +
                "                \"                            <td>睡觉</td>\\n\" +\n" +
                "                \"                        </tr>\\n\" +\n" +
                "                \"                        <tr>\\n\" +\n" +
                "                \"                            <td>3.</td>\\n\" +\n" +
                "                \"                            <td>7:00~10:00</td>\\n\" +\n" +
                "                \"                            <td>睡觉</td>\\n\" +\n" +
                "                \"                        </tr>\\n\" +\n" +
                "                \"                        <tr>\\n\" +\n" +
                "                \"                            <td>4.</td>\\n\" +\n" +
                "                \"                            <td>7:00~10:00</td>\\n\" +\n" +
                "                \"                            <td>睡觉</td>\\n\" +\n" +
                "                \"                        </tr>\\n\" +\n" +
                "                \"                        <tr>\\n\" +\n" +
                "                \"                            <td>5.</td>\\n\" +\n" +
                "                \"                            <td>7:00~10:00</td>\\n\" +\n" +
                "                \"                            <td>睡觉</td>\\n\" +\n" +
                "                \"                        </tr>\\n\" +\n" +
                "                \"                        <tr>\\n\" +\n" +
                "                \"                            <td>6.</td>\\n\" +\n" +
                "                \"                            <td>7:00~10:00</td>\\n\" +\n" +
                "                \"                            <td>睡觉</td>\\n\" +\n" +
                "                \"                        </tr>\\n\" +\n" +
                "                \"                   </table>\\n\" +\n" +
                "                \"                </div>\\n\" +\n" +
                "                \"            </div>\\n\" +\n" +
                "                \"        </div>\\n\" +\n" +
                "                \"    </body>\\n\" +\n" +
                "                \"</html>", "text/html;charset=UTF-8");
        // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
        MimeMultipart mm_text_image = new MimeMultipart();

        mm_text_image.addBodyPart(text);
        mm_text_image.addBodyPart(image);
        mm_text_image.setSubType("related");    // 关联关系

        // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        // 上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
        MimeBodyPart text_image = new MimeBodyPart();
        text_image.setContent(mm_text_image);
        msg.setContent(mm_text_image,"text/html;charset=UTF-8");

//
//        // 9. 创建附件"节点"
//        MimeBodyPart attachment = new MimeBodyPart();
//        // 读取本地文件
//        DataHandler dh2 = new DataHandler(new FileDataSource("src\\mailTestDoc.docx"));
//        // 将附件数据添加到"节点"
//        attachment.setDataHandler(dh2);
//        // 设置附件的文件名（需要编码）
//        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

//        // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
//        MimeMultipart mm = new MimeMultipart();
//        mm.addBodyPart(text_image);
//        mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
//        mm.setSubType("mixed");         // 混合关系

        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
//        msg.setContent(mm);
//        //设置邮件的发送时间,默认立即发送
//        msg.setSentDate(new Date());


        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        return msg;
    }

    public static void main(String[] args) throws Exception {
        sendMail("201543311@m.gduf.edu.cn","验证码","测试邮件是否发送成功");
    }
}
