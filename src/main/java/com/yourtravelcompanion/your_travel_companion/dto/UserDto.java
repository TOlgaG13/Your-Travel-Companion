package com.yourtravelcompanion.your_travel_companion.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String type;
}
