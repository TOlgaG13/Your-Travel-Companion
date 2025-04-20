package com.yourtravelcompanion.your_travel_companion.services;

import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender emailSender;
    private final ApplicationContext applicationContext;


    public EmailServiceImpl(JavaMailSender emailSender, ApplicationContext applicationContext) {
        this.emailSender = emailSender;
        this.applicationContext = applicationContext;
    }

    public void sendMessage(String to, String code) {
        SimpleMailMessage message = applicationContext.getBean(SimpleMailMessage.class);

        System.out.println(to);
        message.setText(code);
        message.setTo(to);
        System.out.println("Email will be sent to: " + to);
        System.out.println("With code: " + code);
        System.out.println("From address: " + message.getFrom());
        emailSender.send(message);
    }

}
