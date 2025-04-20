package com.yourtravelcompanion.your_travel_companion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@Configuration
@EnableScheduling

public class EmailConfig {
    @Value("${spring.mail.username}")
    private String fromAddress;



    @Bean
    public SimpleMailMessage messageTemplate() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("Register code");
        message.setText("Please enter the code into form:\n\n");
        message.setFrom(fromAddress);

        return message;
    }
    @Bean
    public JavaMailSender javaMailSender(@Value("${spring.mail.username}") String username,
                                         @Value("${spring.mail.password}") String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}

