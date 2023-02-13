package com.newcode.meeting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.debug}")
    private String debug;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost(host);
        senderImpl.setPort(port);
        senderImpl.setUsername(username);
        senderImpl.setPassword(password);
        Properties properties = senderImpl.getJavaMailProperties();

        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);
        properties.setProperty("mail.properties.mail.smtp.auth", debug);
        properties.put("mail.smtp.user", "Test Test");
//        Properties properties = senderImpl.getJavaMailProperties();
//        properties.setProperty("mail.transport.protocol", protocol);
//        properties.setProperty("mail.debug", debug);

//        Properties props = senderImpl.getJavaMailProperties();
//        props.put("mail.transport.protocol", protocol);
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.starttls.required", "true");
//        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.debug", debug);
//        props.put("spring.mail.smtp.user", user);
//
//        senderImpl.setJavaMailProperties(props);
        return senderImpl;
    }
}