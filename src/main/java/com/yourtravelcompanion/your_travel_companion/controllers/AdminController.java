package com.yourtravelcompanion.your_travel_companion.controllers;

import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.services.TripService;
import com.yourtravelcompanion.your_travel_companion.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final TripService tripService;

    public AdminController(UserService userService, TripService tripService) {
        this.userService = userService;
        this.tripService = tripService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/toggle")
    public String toggleUserStatus(@RequestParam Long userId,
                                   @AuthenticationPrincipal UserDetails currentUser,
                                   RedirectAttributes redirectAttributes) {

        CustomUser userToToggle = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Користувача не знайдено"));

        if (userToToggle.getEmail().equals(currentUser.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Ви не можете заблокувати самого себе.");
            return "redirect:/admin";
        }

        userService.toggleUserStatus(userId);
        return "redirect:/admin";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("trips", tripService.getAllTrips());
        return "admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/delete-trip/{tripId}")
    public String deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return "redirect:/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser, RedirectAttributes redirectAttributes) {
        CustomUser userToDelete = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Користувач не знайдений"));

        if (userToDelete.getEmail().equals(currentUser.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Ви не можете видалити самого себе.");
            return "redirect:/admin";
        }

        userService.deleteUserById(List.of(id));
        return "redirect:/admin";
    }
}