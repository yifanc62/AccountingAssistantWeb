package com.cirnoteam.accountingassistant.web.controller;

import com.cirnoteam.accountingassistant.web.database.DbException;
import com.cirnoteam.accountingassistant.web.database.RequestException;
import com.cirnoteam.accountingassistant.web.database.UserUtils;
import com.cirnoteam.accountingassistant.web.json.LoginRespEntity;
import com.cirnoteam.accountingassistant.web.json.RegisterRespEntity;
import com.cirnoteam.accountingassistant.web.json.ResetRespEntity;
import com.cirnoteam.accountingassistant.web.json.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Properties;

/**
 * UserController
 * 用户控制器
 *
 * @author Yifan
 * @version 0.5
 */
@Controller
public class UserController {
    @ResponseBody
    @RequestMapping("/login")
    public Response login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String uuid = request.getParameter("uuid");
        String deviceName = request.getParameter("device");
        if (username == null || password == null || uuid == null) {
            return new Response(400, "参数无效！");
        }
        try {
            if (UserUtils.login(username, password)) {
                String token;
                if (UserUtils.isNewDevice(uuid)) {
                    if (deviceName == null || deviceName.equals("")) {
                        return new Response(400, "设备名为空！");
                    }
                    token = UserUtils.addDevice(username, uuid, deviceName);
                } else {
                    token = UserUtils.refreshToken(uuid);
                }
                return new Response(200).setEntity(new LoginRespEntity(token));
            }
            return new Response(500);
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/autologin")
    public Response autologin(HttpServletRequest request) {
        String uuid = request.getParameter("uuid");
        String token = request.getParameter("token");
        if (uuid == null || token == null) {
            return new Response(400, "参数无效！");
        }
        try {
            if (UserUtils.verifyToken(token, uuid)) {
                String newToken = UserUtils.refreshToken(uuid);
                return new Response(200).setEntity(new LoginRespEntity(newToken));
            }
            return new Response(500);
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/register")
    public Response register(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        if (username == null || password == null || email == null) {
            return new Response(400, "参数无效！");
        }
        try {
            String activateToken = UserUtils.addUser(username, password, email);
            if (!sendMail(email, UserUtils.getActivateCode(username), username)) {
                return new Response(400, "激活邮件发送失败！");
            }
            return new Response(200).setEntity(new RegisterRespEntity(activateToken));
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/activate")
    public Response activate(HttpServletRequest request) {
        String username = request.getParameter("username");
        String activateToken = request.getParameter("token");
        String activateCode = request.getParameter("code");
        String uuid = request.getParameter("uuid");
        String deviceName = request.getParameter("device");
        if (activateToken == null || activateCode == null || uuid == null || deviceName == null) {
            return new Response(400, "参数无效！");
        }
        try {
            if (UserUtils.activate(activateToken, activateCode)) {
                String token = UserUtils.addDevice(username, uuid, deviceName);
                return new Response(200).setEntity(new LoginRespEntity(token));
            }
            return new Response(500);
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/authreset")
    public Response authreset(HttpServletRequest request) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        if (username == null || email == null) {
            return new Response(400, "参数无效！");
        }
        try {
            String resetToken = UserUtils.verifyResetAuth(username, email);
            if (!sendMail(email, UserUtils.getResetCode(username), username)) {
                return new Response(400, "重置邮件发送失败！");
            }
            return new Response(200).setEntity(new ResetRespEntity(resetToken));
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/reset")
    public Response reset(HttpServletRequest request) {
        String username = request.getParameter("username");
        String resetToken = request.getParameter("token");
        String resetCode = request.getParameter("code");
        String newPassword = request.getParameter("password");
        if (username == null || resetToken == null || resetCode == null || newPassword == null) {
            return new Response(400, "参数无效！");
        }
        try {
            if (UserUtils.reset(resetToken, resetCode)) {
                UserUtils.resetPassword(username, newPassword);
                return new Response(200);
            }
            return new Response(500);
        } catch (DbException e) {
            return new Response(500, e.getMessage());
        } catch (RequestException e) {
            return new Response(400, e.getMessage());
        }
    }

    /*
    @GetMapping("/avatar")
    public void avatar(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
    }

    @ResponseBody
    @PostMapping("/avatar")
    public Response avatar(HttpServletRequest request) {

    }
    */
    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("index.htm");
    }


    private boolean sendMail(String email, String code, String username) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", "smtp.qq.com");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);
            MimeMessage message = createMimeMessage(session, email, code, username);
            Transport transport = session.getTransport();
            transport.connect("cirnoteam@varkarix.com", "pqsflqfygmwlbfbf");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static MimeMessage createMimeMessage(Session session, String receiveMail, String code, String username) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("cirnoteam@varkarix.com", "Cirno Team", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, username, "UTF-8"));
        message.setSubject("[请勿回复] ⑨号账本邮箱验证码", "UTF-8");
        MimeMultipart contentMulti = new MimeMultipart("related");
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(getMessage(code, username), "text/html;charset=UTF-8");
        contentMulti.addBodyPart(textBody);
        MimeBodyPart body_bg = new MimeBodyPart();
        FileDataSource source_bg = new FileDataSource(new File(URLDecoder.decode(UserController.class.getResource("/bg.jpg").getPath(), "UTF-8")));
        body_bg.setDataHandler(new DataHandler(source_bg));
        body_bg.setContentID("bg");
        MimeBodyPart body_bg2 = new MimeBodyPart();
        FileDataSource source_bg2 = new FileDataSource(new File(URLDecoder.decode(UserController.class.getResource("/bg2.jpg").getPath(), "UTF-8")));
        body_bg2.setDataHandler(new DataHandler(source_bg2));
        body_bg2.setContentID("bg2");
        MimeBodyPart body_logo = new MimeBodyPart();
        FileDataSource source_logo = new FileDataSource(new File(URLDecoder.decode(UserController.class.getResource("/logo.jpg").getPath(), "UTF-8")));
        body_logo.setDataHandler(new DataHandler(source_logo));
        body_logo.setContentID("logo");
        MimeBodyPart body_cirno = new MimeBodyPart();
        FileDataSource source_cirno = new FileDataSource(new File(URLDecoder.decode(UserController.class.getResource("/cirno.png").getPath(), "UTF-8")));
        body_cirno.setDataHandler(new DataHandler(source_cirno));
        body_cirno.setContentID("cirno");
        contentMulti.addBodyPart(body_bg);
        contentMulti.addBodyPart(body_bg2);
        contentMulti.addBodyPart(body_logo);
        contentMulti.addBodyPart(body_cirno);
        MimeBodyPart result = new MimeBodyPart();
        result.setContent(contentMulti);
        MimeMultipart m = new MimeMultipart();
        m.addBodyPart(result);
        message.setContent(m);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private static String getMessage(String code, String username) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0;\">\n" +
                "\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\t\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td style=\"padding: 10px 0 30px 0;\">\n" +
                "\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border: 1px solid #cccccc; border-collapse: collapse;\">\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td align=\"center\" background=\"cid:bg2\" bgcolor=\"#70bbd9\" style=\"padding: 40px 0 30px 0; color: #153643; font-size: 28px; font-weight: bold; font-family: Arial, sans-serif;\">\n" +
                "\t\t\t\t\t\t\t<img src=\"cid:cirno\" width=\"198\" height=\"213\" style=\"display: block;\" />\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td background=\"cid:bg\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                "\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<b><font size=\"5\">尊敬的" + username + "：</font></b>\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<b><font size=\"5\">您的验证码如下</font></b>\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<center><b><font size=\"7\">" + code + "</font></b></center>\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"padding: 20px 0 30px 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t您正在通过⑨号账本的邮件功能激活账号或找回密码，如属您本人操作，请记下您的验证码，并于5分钟内填写于客户端。<br>\n" +
                "\t\t\t\t\t\t\t\t\t\t如非您本人操作，请注意账号安全，建议修改密码。\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"padding: 20px 0 30px 0; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<font color=\"#9D9D9D\">本App是一个基于安卓的个人记账助手应用。用户登陆后，可以进行日常流水的记录，记录后的数据可以以每日、每月或每年的形式通过统计图表来查看账单详情，方便用户进行接下来的财务管理。流水可进行详细的分类。账本也可通过其他社交软件进行分享。</font>\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t<td bgcolor=\"#2894FF\" style=\"padding: 30px 30px 30px 30px;\">\n" +
                "\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td style=\"color: #ffffff; font-family: Arial, sans-serif; font-size: 14px;\" width=\"75%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t&reg; Cirno Team 2017<br/>\n" +
                "\t\t\t\t\t\t\t\t\t\t第⑨小队竭诚为您服务\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t<td align=\"right\" width=\"25%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-size: 0; line-height: 0;\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<a style=\"color: #ffffff;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"cid:logo\" width=\"70\" height=\"70\" style=\"display: block;\" border=\"0\" />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t</table>\n" +
                "</body>\n" +
                "</html>";
    }
}
