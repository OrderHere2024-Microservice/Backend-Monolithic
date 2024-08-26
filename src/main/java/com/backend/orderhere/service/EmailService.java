package com.backend.orderhere.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

  public void sendEmailWithCode(String recipientEmail, String code) throws MessagingException {
    // gmail service setting
    String host = "smtp.gmail.com";
//        String port = "587"; //TLS
    String port = "465"; //SSL
    String username = "orderhereemailservice@gmail.com";
//        String password = "Orderhere123";
    String password = "qrfvnbffmxbqtmmh";
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", Integer.parseInt(port));
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.socketFactory.port", Integer.parseInt(port));
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.socketFactory.fallback", "false");

    Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    // create message
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
      message.setSubject("Reset New Password");
      String emailContent = "Hello,<br><br>" +
          "We've received a request to reset the password for your OrderHere account.<br><br>" +
          "Please use the following verification code to continue: <span style=\"color: red;\">" + code + "</span><br><br>" +
          "Kindly note, this code will expire in 30 minutes. If you did not make this request, please ignore this email.";
      message.setContent(emailContent, "text/html");

      // send email
      Transport.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
