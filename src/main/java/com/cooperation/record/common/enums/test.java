package com.cooperation.record.common.enums;

import com.cooperation.record.utils.MailUtil;

/**
 * @author
 * @date
 */
public class test {

    public static void main(String[] args) throws Exception {
//        MailUtil.sendMail("1974023122@qq.com","\n" +
//                "<html>\n" +
//                "    <head>\n" +
//                "        <title>手机接收</title>\n" +
//                "        <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=0.5,\n" +
//                "        maximum-scale=2.0, user-scalable=no\" charset=\"UTF-8\"/>\n" +
//                "         <style type=\"text/css\">\n" +
//                "                body{\n" +
//                "                    /* min-width:1260px; */\n" +
//                "                    margin: 0 auto;\n" +
//                "                    padding: 0;\n" +
//                "                }\n" +
//                "                .wrap{\n" +
//                "                    /* width: 360px;\n" +
//                "                    height: 761px; */\n" +
//                "                    width: 100%;\n" +
//                "                    height: 860px;\n" +
//                "                    margin: 0 auto;\n" +
//                "                    background-color: #FFF;\n" +
//                "                }\n" +
//                "                .wrap-inner{\n" +
//                "                    width: 96%;\n" +
//                "                    height: 98%;\n" +
//                "                    margin:20px  auto;\n" +
//                "                    border: 1px solid #979595;\n" +
//                "                    border-radius: 2%;\n" +
//                "                }\n" +
//                "                .wrap-pic{\n" +
//                "                    /* width: 320px;\n" +
//                "                    height: 320px; */\n" +
//                "                    width: 94%;\n" +
//                "                    height: 28%;\n" +
//                "                    margin:20px auto 0;\n" +
//                "                    border-radius: 10px;\n" +
//                "                    background-color: #5F5F5F;\n" +
//                "                }\n" +
//                "                .wrap-pic img{\n" +
//                "                    width: 100%;\n" +
//                "                    height: 100%;\n" +
//                "                    /* border: none; */\n" +
//                "                    border-radius: 10px;\n" +
//                "                }\n" +
//                "                .resume{\n" +
//                "                    /* width: 320px;\n" +
//                "                    height: 230px; */\n" +
//                "                    /* position: relative; */\n" +
//                "                    /* width: 90%; */\n" +
//                "                    height: 26%;\n" +
//                "                    margin: -50px auto 0;\n" +
//                "                    text-align: center;\n" +
//                "                }\n" +
//                "                .resume-top{\n" +
//                "                    /* position: absolute;\n" +
//                "                    top: -50px;\n" +
//                "                    left: 48%; */\n" +
//                "                    width: inherit;\n" +
//                "                    /* height: 100px; */\n" +
//                "                    /* height: 44%; */\n" +
//                "                    z-index: 1;\n" +
//                "                    /* margin:20px 0; */\n" +
//                "                }\n" +
//                "                .resume-circle{\n" +
//                "                    display: inline-block;\n" +
//                "                    width: 150px;\n" +
//                "                    height: 150px;\n" +
//                "                    /* width: 31%;\n" +
//                "                    height: 100%; */\n" +
//                "                    border-radius: 50%;\n" +
//                "                    background-color: #233560 ;\n" +
//                "                    /* vertical-align: middle; */\n" +
//                "                }\n" +
//                "                .resume-circle img{\n" +
//                "                    width: inherit;\n" +
//                "                    height: inherit;\n" +
//                "                    border-radius: 50%;\n" +
//                "                }\n" +
//                "                .resume-name{\n" +
//                "                    /* display: inline-block; */\n" +
//                "                    /* width: 90px;\n" +
//                "                    height: 30px; */\n" +
//                "                    /* width: 90px; */\n" +
//                "                    /* height: 100%; */\n" +
//                "                    height: 13%;\n" +
//                "                    margin: 5px auto 0;\n" +
//                "                    /* margin-left: 20px; */\n" +
//                "                    /* margin-left: 2%; */\n" +
//                "                    color: #333;\n" +
//                "                    font-size: 19px;\n" +
//                "                    font-weight: 400;\n" +
//                "                    text-align: center;\n" +
//                "                    /* line-height: 100%; */\n" +
//                "                    /* vertical-align: middle; */\n" +
//                "                }\n" +
//                "                .resume-middle{\n" +
//                "                    /* width: inherit; */\n" +
//                "                    /* height: 25px; */\n" +
//                "                    /* height: 20%; */\n" +
//                "                    height: 12%;\n" +
//                "                    /* margin: 10px auto 0; */\n" +
//                "                    color: #333;\n" +
//                "                    font-size: 13px;\n" +
//                "                    line-height: 25px;\n" +
//                "                    text-align: center;\n" +
//                "                }\n" +
//                "                .planing{\n" +
//                "                    width: 94%;\n" +
//                "                    margin: 0 auto;\n" +
//                "                }\n" +
//                "                .planing-title{\n" +
//                "                    height: 70px;\n" +
//                "                    margin: 0 auto;\n" +
//                "                    background: url(images/plan.png) no-repeat 51% 84%;\n" +
//                "                    background-size: 209px 11px;\n" +
//                "                    text-align: center;\n" +
//                "                }\n" +
//                "                .planing-title img{\n" +
//                "                    width: 49px;\n" +
//                "                    height: 46px;\n" +
//                "                    vertical-align: middle;\n" +
//                "                }\n" +
//                "                .planing-title span{\n" +
//                "                    margin-left: 10px;\n" +
//                "                    color: #333;\n" +
//                "                    font-size: 15px;\n" +
//                "                    font-weight: 400;\n" +
//                "                    opacity: .98;\n" +
//                "                    vertical-align: middle;\n" +
//                "                }\n" +
//                "                .planing p{\n" +
//                "                    color: #333;\n" +
//                "                    font-size: 13px;\n" +
//                "                    font-weight: 400;\n" +
//                "                    opacity: .98;\n" +
//                "                }\n" +
//                "                .plan-content{\n" +
//                "                    width: 100%;\n" +
//                "                }\n" +
//                "                table{\n" +
//                "                    color: #333;\n" +
//                "                    font-size: 16px;\n" +
//                "                    font-weight: 400;\n" +
//                "                    opacity: .98;\n" +
//                "                }\n" +
//                "                tr{\n" +
//                "                    height: 40px;\n" +
//                "                    background: url(images/Wavyline.png) no-repeat 26px 100%;\n" +
//                "                    background-size: contain;\n" +
//                "                    \n" +
//                "                }\n" +
//                "                table tr td{\n" +
//                "                    padding: 0 5px;\n" +
//                "                }\n" +
//                "\n" +
//                "         </style>\n" +
//                "        <!-- <link href=\"css/phone.css\" rel=\"stylesheet\"  type=\"text/css\"> -->\n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                "        <div class=\"wrap\">\n" +
//                "            <div class=\"wrap-inner\">\n" +
//                "                <div class=\"wrap-pic\">\n" +
//                "                    <img>\n" +
//                "                </div>\n" +
//                "                <div class=\"resume\">\n" +
//                "                    <div class=\"resume-top\">\n" +
//                "                        <div class=\"resume-circle\">\n" +
//                "                            <img>\n" +
//                "                        </div>\n" +
//                "                        <div class=\"resume-name\">名字</div>\n" +
//                "                        <div class=\"resume-middle\">\n" +
//                "                            \"好好学习，天天向上\"\n" +
//                "                        </div>\n" +
//                "                    </div>\n" +
//                "                </div>\n" +
//                "               <div class=\"planing\">\n" +
//                "                   <div class=\"planing-title\">\n" +
//                "                       <img src=\"images/sun.png\">\n" +
//                "                       <span>今日计划清单</span>\n" +
//                "                    </div>\n" +
//                "                    <p>2021/10/22</p>\n" +
//                "                    <p>Thur.</p>\n" +
//                "                   <table >\n" +
//                "                       <tr>\n" +
//                "                           <td>1.</td>\n" +
//                "                           <td>5:00~6:30</td>\n" +
//                "                           <td>背单词</td>\n" +
//                "                       </tr>\n" +
//                "                       <tr>\n" +
//                "                            <td>2.</td>\n" +
//                "                            <td>7:00~10:00</td>\n" +
//                "                            <td>睡觉</td>\n" +
//                "                        </tr>\n" +
//                "                        <tr>\n" +
//                "                            <td>3.</td>\n" +
//                "                            <td>7:00~10:00</td>\n" +
//                "                            <td>睡觉</td>\n" +
//                "                        </tr>\n" +
//                "                        <tr>\n" +
//                "                            <td>4.</td>\n" +
//                "                            <td>7:00~10:00</td>\n" +
//                "                            <td>睡觉</td>\n" +
//                "                        </tr>\n" +
//                "                        <tr>\n" +
//                "                            <td>5.</td>\n" +
//                "                            <td>7:00~10:00</td>\n" +
//                "                            <td>睡觉</td>\n" +
//                "                        </tr>\n" +
//                "                        <tr>\n" +
//                "                            <td>6.</td>\n" +
//                "                            <td>7:00~10:00</td>\n" +
//                "                            <td>睡觉</td>\n" +
//                "                        </tr>\n" +
//                "                   </table>\n" +
//                "                </div>\n" +
//                "            </div>\n" +
//                "        </div>\n" +
//                "    </body>\n" +
//                "</html>","测试");
        MailUtil.sendMail("1974023122@qq.com");
    }
}
