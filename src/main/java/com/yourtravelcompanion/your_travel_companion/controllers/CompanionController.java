package com.yourtravelcompanion.your_travel_companion.controllers;

import com.yourtravelcompanion.your_travel_companion.models.Companion;
import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.models.Trip;
import com.yourtravelcompanion.your_travel_companion.services.CompanionService;

import com.yourtravelcompanion.your_travel_companion.services.TripService;
import com.yourtravelcompanion.your_travel_companion.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




@Controller
public class CompanionController {
    private final CompanionService companionService;
    private final TripService tripService;
    private final UserService userService;

    public CompanionController(CompanionService companionService, TripService tripService, UserService userService) {
        this.companionService = companionService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/companions")
    public String showUserCompanions(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        var user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("companions", companionService.getCompanionsByUserId(user.getId()));
        return "companions";
    }

    @PostMapping("/companions/{id}/remove")
    public String removeCompanion(@PathVariable Long id,
                                  @AuthenticationPrincipal Object principal) {

        Companion companion = companionService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Companion not found"));

        Trip trip = companion.getTrip();
        String email = userService.getCurrentUserEmail();
        CustomUser currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found by email"));

        if (trip.getUser().getId() != currentUser.getId()) {
            throw new SecurityException("You are not authorized to remove companions from this trip.");
        }

        companionService.deleteCompanion(id);
        return "redirect:/trips/" + trip.getId();
    }

}