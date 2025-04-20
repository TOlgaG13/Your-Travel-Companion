package com.yourtravelcompanion.your_travel_companion.services;

import com.yourtravelcompanion.your_travel_companion.models.Country;
import com.yourtravelcompanion.your_travel_companion.repositories.CountryRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // Повернення списку усіх країн
    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    // Додавання нової країни
    @Transactional
    public Country addCountry(String name) {
        Country country = new Country();
        country.setName(name);
        return countryRepository.save(country);
    }

    // Повернення крайни по ід
    @Transactional(readOnly = true)
    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }

    // Видалення країни по ід
    @Transactional
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }
}

