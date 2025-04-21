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




    @Bean
    public SimpleMailMessage messageTemplate() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Register code");
        message.setText("Please enter the code into form:\n\n");
        message.setFrom(System.getenv("MAIL_USERNAME"));
        return message;
    }

    }


