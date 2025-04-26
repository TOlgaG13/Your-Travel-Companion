package com.yourtravelcompanion.your_travel_companion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    public static final String ADMIN_LOGIN = "admin@gmail.com";

    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler
                        ) {this.authenticationSuccessHandler = authenticationSuccessHandler;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/code", "/oauth2/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/css/**", "/images/**").permitAll()

                        .requestMatchers("/trips/**", "/companions/**", "/account/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/account", false)

                        .failureHandler(authenticationFailureHandler())

                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)//очистити сесію,запоб.повт.використ сесії
                        .deleteCookies("JSESSIONID")//ідентифікує сесію юсера
                        .permitAll()
                )
                .requestCache(request -> request.disable())

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login"))
                );

        return http.build();
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            if (exception.getCause() instanceof LockedException)
            {
                response.sendRedirect("/login?blocked=true");
            } else {
                response.sendRedirect("/login?error=true");
            }
        };
    }
}