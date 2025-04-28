package com.yourtravelcompanion.your_travel_companion.config;

import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.models.UserRegisterType;
import com.yourtravelcompanion.your_travel_companion.models.UserRole;
import com.yourtravelcompanion.your_travel_companion.services.UserDetailsServiceImpl;
import com.yourtravelcompanion.your_travel_companion.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@Component
public class AuthHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public AuthHandler(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.getOrDefault("email", "");
        CustomUser user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            String randomPassword = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(randomPassword);

            CustomUser customUser = new CustomUser();
            customUser.setLogin(email.split("@")[0]);
            customUser.setPassword(encodedPassword);
            if (email.equals("admin@gmail.com")) {
                customUser.setRole(UserRole.ADMIN);
            } else {
                customUser.setRole(UserRole.USER);
            }
            customUser.setType(UserRegisterType.GOOGLE);
            customUser.setEmail(email);
            customUser.setPhone("");
            customUser.setAddress("");
            customUser.setTime(LocalDateTime.now());
            customUser.setActive(true);

            userService.addUser(customUser);
            user = customUser;
        } else {
            if (!user.getType().equals(UserRegisterType.GOOGLE)) {
                request.getSession().setAttribute("oauth_email", email);
                response.sendRedirect("/link-google-account");
                return;
            }

            if (user.isBlocked()) {
                request.getSession().invalidate();
                response.sendRedirect("/login?blocked=true");
                return;
            }

        }
        if (user != null && user.getRole() != UserRole.ADMIN) {
            String redirectUrl = (String) request.getSession().getAttribute("redirect_url");

            if (redirectUrl == null || redirectUrl.contains("/login") ||redirectUrl.contains("/admin") || redirectUrl.contains("/register")) {
                redirectUrl = "/account";
            }
            System.out.println("Redirecting to: " + redirectUrl);

            response.sendRedirect(redirectUrl);
        }
    }

    @Bean
    public CommandLineRunner createAdmin(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";
            if (userService.findByEmail(adminEmail).isEmpty()) {
                CustomUser admin = new CustomUser();
                admin.setEmail(adminEmail);
                admin.setLogin("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(UserRole.ADMIN);
                admin.setActive(true);

                userService.addUser(admin);
                System.out.println("Admin created!");
            }
        };
    }

}
