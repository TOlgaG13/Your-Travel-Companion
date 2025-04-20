package com.yourtravelcompanion.your_travel_companion.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendMessage(String to, String code);
}

