package com.example.dacs1.Utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

public class EmailSender {

    private static final String EMAIL = "guimaresetmk@gmail.com";
    private static final String PASSWORD = "nyvi pjyb cmlq hzvi";

    public static boolean sendVerificationEmail(String recipient, String code) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            String noiDung = "<!DOCTYPE html>" +
                    "<html lang='vi'>" +
                    "<head><meta charset='UTF-8'></head>" +
                    "<body style='font-family: Arial, sans-serif; margin:0; padding:0;'>" +
                    "<table align='center' width='100%' cellpadding='0' cellspacing='0' style='background-color:#f9f9f9; padding: 24px;'>" +
                    "  <tr>" +
                    "    <td>" +
                    "      <table align='center' cellpadding='0' cellspacing='0' style='max-width: 500px; background-color: #ffffff; border: 1px solid #ccc; border-radius: 8px; padding: 20px;'>" +
                    "        <tr>" +
                    "          <td align='center' style='color:#203A43; font-size: 20px; font-weight: bold;'>HỆ THỐNG QUẢN LÝ TRƯỜNG HỌC</td>" +
                    "        </tr>" +
                    "        <tr><td height='20'></td></tr>" +
                    "        <tr>" +
                    "          <td style='font-size:14px;'>Mã xác nhận của bạn là: <strong style='font-size:16px;'>" + code + "</strong></td>" +
                    "        </tr>" +
                    "        <tr><td height='10'></td></tr>" +
                    "        <tr>" +
                    "          <td style='font-size:14px; color: red;'><strong>⚠ Vui lòng KHÔNG gửi mã cho người khác!</strong></td>" +
                    "        </tr>" +
                    "        <tr><td height='10'></td></tr>" +
                    "        <tr>" +
                    "          <td style='font-size:10px; color:#999;'>Thời gian gửi: " + java.time.LocalDateTime.now() + "</td>" +
                    "        </tr>" +
                    "      </table>" +
                    "    </td>" +
                    "  </tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";



            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(noiDung, "text/html; charset=UTF-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Mã xác nhận đặt lại mật khẩu");
            message.setContent(multipart);

            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
