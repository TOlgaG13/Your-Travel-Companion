package com.yourtravelcompanion.your_travel_companion.services;

import com.yourtravelcompanion.your_travel_companion.config.SecurityConfig;
import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.models.Trip;
import com.yourtravelcompanion.your_travel_companion.models.UserRegisterType;
import com.yourtravelcompanion.your_travel_companion.models.UserRole;
import com.yourtravelcompanion.your_travel_companion.repositories.CompanionRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.TripRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
private final CompanionRepository companionRepository;
private final TripRepository tripRepository;
private final TripService tripService;
    private final EmailService emailService;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanionRepository companionRepository, TripRepository tripRepository, TripService tripService,EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companionRepository = companionRepository;
        this.tripRepository = tripRepository;
        this.tripService = tripService;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public List<CustomUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CustomUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUserById(List<Long> ids) {
        ids.forEach(id -> {
            Optional<CustomUser> user = userRepository.findById(id);
            user.ifPresent(u -> {
                if (!SecurityConfig.ADMIN_LOGIN.equals(u.getEmail())) {
                    //  видалення заявки користувача як компаньйона
                    companionRepository.deleteAllByUserId(u.getId());

                    // Якщо є поїздки, створені цим користувачем — видалити їх
                    List<Trip> userTrips = tripRepository.findByUserId(u.getId());
                    userTrips.forEach(trip -> tripService.deleteTrip(trip.getId()));
                    userRepository.deleteById(u.getId());
                }
            });
        });
    }

    @Transactional
    public void addUser(String login,
                        String password,
                        UserRole role,
                        UserRegisterType registerType,
                        String email,
                        String phone,
                        String address) {
        CustomUser user = new CustomUser();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        user.setType(registerType);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(String email, String phone, String address) {
        CustomUser user = userRepository.findByEmail(email);
        if (user == null)
            return;
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void addUser(CustomUser customUser) {
        userRepository.save(customUser);
    }

    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<CustomUser> findByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login));
    }

    @Transactional(readOnly = true)
    public boolean existsByLogin(String login) {
       return userRepository.existsByLogin(login);
   }

    @Transactional(readOnly = true)
    public Optional<CustomUser> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
    @Transactional
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        //звич.форма реєст.
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
            //берем напряму емайл
        } else if (principal instanceof OidcUser oidcUser) {
            return oidcUser.getEmail(); // Google OAuth
            //емаіл з атрибутів(не викор).
        } else if (principal instanceof OAuth2User oauth2User) {
            return (String) oauth2User.getAttribute("email");
        }

        throw new IllegalStateException("Unable to extract user email");
    }
    @Transactional
    public void toggleUserStatus(Long id) {
        CustomUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBlocked(!user.isBlocked());

        userRepository.save(user);
    }
    @Transactional
    public void sendEmailChangeCode(String oldEmail, String newEmail) {
        CustomUser user = userRepository.findByEmail(oldEmail);
        if (user == null) {
            return;
        }

        String code = generateVerificationCode();
        user.setTempEmail(newEmail);
        user.setVerificationCode(code);
        userRepository.save(user);


        emailService.sendMessage(newEmail, "Your confirmation code is: " + code);
    }
    @Transactional
    public boolean confirmEmailChange(String currentEmail, String code) {
        CustomUser user = userRepository.findByEmail(currentEmail);
        if (user == null) {
            return false;
        }

        if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            user.setEmail(user.getTempEmail());        // стар.на нов.
            user.setTempEmail(null);                   // обнул.тимчасовий емаіл
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }
    private String generateVerificationCode() {
        int code = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(code);
    }

}


