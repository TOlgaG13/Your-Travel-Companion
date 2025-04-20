package com.yourtravelcompanion.your_travel_companion.controllers;

import com.yourtravelcompanion.your_travel_companion.models.Country;
import com.yourtravelcompanion.your_travel_companion.services.CountryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/countries")
    public String listCountries(Model model) {
        model.addAttribute("countries", countryService.getAllCountries());
        return "countries";
    }

    @GetMapping("/countries/create")
    public String showCreateForm(Model model) {
        model.addAttribute("country", new Country());
        return "create-country";
    }

    @PostMapping("/countries/create")
    public String createCountry(@ModelAttribute Country country) {
        countryService.addCountry(country.getName());
        return "redirect:/countries";
    }

    @PostMapping("/countries/{id}/delete")
    public String deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return "redirect:/countries";
    }
}


