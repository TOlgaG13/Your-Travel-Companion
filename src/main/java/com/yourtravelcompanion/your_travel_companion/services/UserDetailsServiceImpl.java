package com.yourtravelcompanion.your_travel_companion.services;

import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public UserDetailsServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser customUser = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (customUser.isBlocked()) {
            System.out.println(" User is locked,LockedException");
            throw new InternalAuthenticationServiceException("User account is locked", new LockedException("User is locked"));

        }

        return new CustomUserDetails(customUser);
    }

    }




