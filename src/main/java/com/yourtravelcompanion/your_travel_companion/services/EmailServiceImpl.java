package com.yourtravelcompanion.your_travel_companion.services;

import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender emailSender;
    private final SimpleMailMessage template;

    public EmailServiceImpl(JavaMailSender emailSender, SimpleMailMessage template) {
        this.emailSender = emailSender;
        this.template = template;
    }

    @Override
    public void sendMessage(String to, String code) {

        SimpleMailMessage message = new SimpleMailMessage(template);
        message.setTo(to);
        message.setText(code);

        System.out.println("Email will be sent to: " + to);
        System.out.println("With code: " + code);
        System.out.println("From address: " + message.getFrom());

        emailSender.send(message);
    }
}
