package com.yourtravelcompanion.your_travel_companion.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Table(name="users")
public class CustomUser {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long id;

 private String login;
 private String password;

 @Enumerated(EnumType.STRING)
 private UserRole role;
 @Enumerated(EnumType.STRING)
 private UserRegisterType type;
 private boolean active = true;

 private String email;
 private String phone;
 private String address;

 private LocalDateTime time;
 private boolean blocked;

 private String tempEmail;
 private String verificationCode;

 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
 private List<Trip> trips;

 public CustomUser() {
 }

 public CustomUser(String login, String password, UserRole role, String email, String phone, String address, UserRegisterType type, String tempEmail, String verificationCode) {
  this.login = login;
  this.password = password;
  this.role = role;
  this.email = email;
  this.phone = phone;
  this.address = address;
  this.type = type;
  this.tempEmail = tempEmail;
  this.verificationCode = verificationCode;
 }
}



