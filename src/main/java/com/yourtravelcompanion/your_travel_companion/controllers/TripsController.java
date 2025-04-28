package com.yourtravelcompanion.your_travel_companion.controllers;

import com.yourtravelcompanion.your_travel_companion.dto.TripDto;
import com.yourtravelcompanion.your_travel_companion.models.Companion;
import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.models.Trip;
import com.yourtravelcompanion.your_travel_companion.services.CompanionService;
import com.yourtravelcompanion.your_travel_companion.services.CountryService;
import com.yourtravelcompanion.your_travel_companion.services.TripService;
import com.yourtravelcompanion.your_travel_companion.services.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TripsController {
    private final TripService tripService;
    private final UserService userService;
    private final CountryService countryService;
private final CompanionService companionService;

    public TripsController(TripService tripService, UserService userService, CountryService countryService, CompanionService companionService) {
        this.tripService = tripService;
        this.userService = userService;
        this.countryService = countryService;
        this.companionService = companionService;
    }

    @GetMapping("/trips")
    public String listTrips(Model model) {
        List<TripDto> trips = tripService.getAllTrips().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        model.addAttribute("trips", trips);
        return "trips";
    }

    @GetMapping("/trips/create")
    public String showCreateForm(Model model) {
        model.addAttribute("trip", new TripDto());
        model.addAttribute("countries", countryService.getAllCountries());
        return "create-trip";
    }

    @PostMapping("/trips/create")
    public String createTrip(@ModelAttribute TripDto tripDto, @AuthenticationPrincipal Object principal) {
        String email = getEmailFromPrincipal(principal);
        CustomUser user = userService.findByEmail(email).orElseThrow();

        tripService.createTrip(tripDto, user);
        return "redirect:/trips";
    }

    @GetMapping("/trips/{id}")
    public String viewTrip(@PathVariable Long id, @AuthenticationPrincipal Object principal, Model model) {
        Trip trip = tripService.getTripById(id)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));

        model.addAttribute("trip", trip);
        model.addAttribute("companions", trip.getCompanions());

        if (principal != null) {
            String email = getEmailFromPrincipal(principal);
            CustomUser user = userService.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            boolean joined = trip.getCompanions().stream()
                    .anyMatch(c -> c.getUser().getId() == user.getId());

            model.addAttribute("joined", joined);
            model.addAttribute("user", user);
        }
        return "trip-details";
    }

    @GetMapping("/trips/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Trip trip = tripService.getTripById(id)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));
        model.addAttribute("trip", convertToDto(trip));
        return "create-trip";
    }

    @PostMapping("/trips/{id}/edit")
    public String updateTrip(@PathVariable Long id, @ModelAttribute TripDto tripDto) {
        tripService.updateTrip(id, tripDto);
        return "redirect:/trips";
    }


    @GetMapping("/trips/search")
    public String searchTrips(@RequestParam(required = false) String destination,
                              @RequestParam(required = false) String country,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              Model model) {
        List<TripDto> trips = tripService.searchTrips(destination, country, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        model.addAttribute("trips", trips);
        return "trips";
    }

    @PostMapping("/trips/{id}/join")
    public String joinTrip(@PathVariable Long id, @AuthenticationPrincipal Object principal) {
        String email = getEmailFromPrincipal(principal);
        CustomUser user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        companionService.joinTrip(id, user.getId());

        return "redirect:/trips";
    }


    @PostMapping("/trips/{id}/leave")
    public String leaveTrip(@PathVariable("id") Long tripId,
                            @AuthenticationPrincipal Object principal) {
        String email = getEmailFromPrincipal(principal);
        CustomUser user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));
        System.out.println("Leave request from userId=" + user.getId() + " for tripId=" + tripId);

        if (trip.getUser().getId() == user.getId()) {
            System.out.println("The author of the trip cannot leave it");
            return "redirect:/account";
        }
        companionService.leaveTrip(tripId, user.getId());

        return "redirect:/account";
    }


    @PostMapping("/trips/{id}/delete")
    public String deleteTrip(@PathVariable Long id, @AuthenticationPrincipal Object principal) {
        String email = getEmailFromPrincipal(principal);
        CustomUser user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Trip trip = tripService.getTripById(id)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));

        //чи юсер автор поїздки?
        if (trip.getUser().getId() != user.getId()) {

            return "redirect:/trips";
        }

        tripService.deleteTrip(id);
        return "redirect:/account";
    }

    private TripDto convertToDto(Trip trip) {
        TripDto dto = new TripDto();
        dto.setId(trip.getId());
        dto.setDestination(trip.getDestination());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setDescription(trip.getDescription());
        dto.setCreated(trip.getCreated());
        dto.setUserId(trip.getUser().getId());
        dto.setCountryId(trip.getCountry().getId());
        return dto;
    }
    private String getEmailFromPrincipal(Object principal) {
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof OAuth2User oAuth2User) {
            return (String) oAuth2User.getAttribute("email");
        }
        throw new IllegalStateException("Unknown user type");
    }



}