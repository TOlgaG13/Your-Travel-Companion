package com.yourtravelcompanion.your_travel_companion.controllers;

import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.models.Trip;
import com.yourtravelcompanion.your_travel_companion.models.UserRegisterType;
import com.yourtravelcompanion.your_travel_companion.models.UserRole;
import com.yourtravelcompanion.your_travel_companion.repositories.CompanionRepository;
import com.yourtravelcompanion.your_travel_companion.services.CompanionService;
import com.yourtravelcompanion.your_travel_companion.services.EmailService;
import com.yourtravelcompanion.your_travel_companion.services.TripService;
import com.yourtravelcompanion.your_travel_companion.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TripService tripService;
    private final CompanionService companionService;
    private final CompanionRepository companionRepository;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, EmailService emailService, TripService tripService, CompanionService companionService, CompanionRepository companionRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tripService = tripService;
        this.companionService = companionService;
        this.companionRepository = companionRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String login,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String address,
                               HttpSession session,
                               Model model) {

        if (userService.existByEmail(email)) {
            model.addAttribute("error", "Email is already in use!");
            return "register";
        }

        String code = String.valueOf((int) (Math.random() * 9000) + 1000);

        session.setAttribute("login", login);
        session.setAttribute("password", passwordEncoder.encode(password));
        session.setAttribute("email", email);
        session.setAttribute("phone", phone);
        session.setAttribute("address", address);
        session.setAttribute("trueCode", code);

        emailService.sendMessage(email, code);

        return "redirect:/code";
    }

    @GetMapping("/code")
    public String showCodeForm() {
        return "code";
    }

    @PostMapping("/code")
    public String verifyCode(@RequestParam String inputCode,
                             HttpSession session,
                             Model model) {

        String trueCode = (String) session.getAttribute("trueCode");

        if (trueCode != null && trueCode.equals(inputCode)) {
            userService.addUser(
                    (String) session.getAttribute("login"),
                    (String) session.getAttribute("password"),
                    UserRole.USER,
                    UserRegisterType.FORM,
                    (String) session.getAttribute("email"),
                    (String) session.getAttribute("phone"),
                    (String) session.getAttribute("address")
            );
            session.invalidate();
            return "redirect:/login";
        } else {
            model.addAttribute("errorCode", true);
            return "code";
        }
    }


    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        if (referrer != null && !referrer.contains("/login") && !referrer.contains("/logout")) {
            request.getSession().setAttribute("redirect_url", referrer);
        }//повертаємо юсера звідки прийшов
        return "login";
    }


    @GetMapping("/account")
    public String showAccount(@AuthenticationPrincipal Object principal,
                              Model model) {
        String email = getEmailFromPrincipal(principal);

        CustomUser dbUser = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        //Поїздки,які створив юсер
        List<Trip> createdTrips = tripService.getTripsWithCompanionsByUserId(dbUser.getId());

        //Поїздки, до яких юсер приєднався, але не він створював(не є автором поїздок)
        List<Trip> companionTrips = companionRepository.findTripsWhereUserIsOnlyCompanion(dbUser.getId()).stream()
                .peek(trip -> trip.setCompanions(companionRepository.findByTripId(trip.getId())))
                .toList();


        System.out.println("Created Trips: " + createdTrips.size());
        System.out.println("Joined Trips: " + companionTrips.size());

        model.addAttribute("user", dbUser);
        model.addAttribute("createdTrips", createdTrips);
        model.addAttribute("companionTrips", companionTrips);
        model.addAttribute("companions", companionService.getCompanionsByUserId(dbUser.getId()));

        return "account";
    }

    @GetMapping("/account/edit")
    public String editAccount(@AuthenticationPrincipal Object principal, Model model) {
        String email = getEmailFromPrincipal(principal);
        CustomUser user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        model.addAttribute("user", user);
        return "edit-account";
    }

    @PostMapping("/account/edit")
    public String updateAccount(@AuthenticationPrincipal Object principal,
                                @RequestParam String phone,
                                @RequestParam String address) {
        String email = getEmailFromPrincipal(principal);
        userService.updateUser(email, phone, address);
        return "redirect:/account";
    }


    //доп. мет. для визначеня пот. кор.для зчит.емайлу

    private String getEmailFromPrincipal(Object principal) {
        //автор.через форму
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();//username=email
        } else if (principal instanceof DefaultOidcUser oAuth2User) {
            return (String) oAuth2User.getAttributes().get("email");
        }
        throw new IllegalStateException("Unknown user type");
    }

}

