package com.newcode.meeting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSender {

    @Autowired
    JavaMailSender getJavaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void send(String emailTo, String subject, String messageText){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageText);

        getJavaMailSender.send(mailMessage);
    }

    public  void sendHtml(String emailTo, String subject, String messageText) throws MessagingException {
        MimeMessage message = getJavaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,  "UTF-8");
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        getJavaMailSender.send(message);
    }
}