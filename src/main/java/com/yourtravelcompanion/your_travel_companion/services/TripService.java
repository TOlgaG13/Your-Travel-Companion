package com.yourtravelcompanion.your_travel_companion.services;

import com.yourtravelcompanion.your_travel_companion.dto.TripDto;
import com.yourtravelcompanion.your_travel_companion.models.*;
import com.yourtravelcompanion.your_travel_companion.repositories.CompanionRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.CountryRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.TripRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CompanionRepository companionRepository;

    public TripService(TripRepository tripRepository,
                       UserRepository userRepository,
                       CountryRepository countryRepository,
                       CompanionRepository companionRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.companionRepository = companionRepository;
    }

    @Transactional
    public void createTrip(TripDto tripDto, CustomUser user) {
        Trip trip = new Trip();
        trip.setDestination(tripDto.getDestination());
        trip.setStartDate(tripDto.getStartDate());
        trip.setEndDate(tripDto.getEndDate());
        trip.setDescription(tripDto.getDescription());
        trip.setCreated(LocalDateTime.now());
        trip.setUser(user);
        Country country = countryRepository.findById(tripDto.getCountryId())
                .orElseThrow(() -> new IllegalStateException("Країна не знайдена"));
        trip.setCountry(country);

        tripRepository.save(trip);
        Companion companion = new Companion();
        companion.setTrip(trip);
        companion.setUser(user);


        companionRepository.save(companion);
    }

    @Transactional
    public void updateTrip(Long id, TripDto updatedTrip) {
        tripRepository.findById(id).ifPresent(trip -> {
            trip.setDestination(updatedTrip.getDestination());
            trip.setStartDate(updatedTrip.getStartDate());
            trip.setEndDate(updatedTrip.getEndDate());
            trip.setDescription(updatedTrip.getDescription());
            trip.setCountry(
                    countryRepository.findById(updatedTrip.getCountryId())
                            .orElseThrow(() -> new IllegalStateException("Країна не знайдена"))
            );
            tripRepository.save(trip);
        });
    }

    @Transactional
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }


    @Transactional
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findByIdWithCompanions(id);
    }


    @Transactional
    public List<Trip> searchTrips(String destination, String country, LocalDate startDate, LocalDate endDate) {
        if (destination != null && !destination.isEmpty()) {
            return tripRepository.findByDestinationContainingIgnoreCase(destination);
        } else if (country != null && !country.isEmpty()) {
            return tripRepository.findByCountry_NameContainingIgnoreCase(country);
        } else if (startDate != null && endDate != null) {
            return tripRepository.findByStartDateBetween(startDate, endDate);
        }
        return tripRepository.findAll();
    }

    @Transactional
    public List<Trip> getTripsWithCompanionsByUserId(Long userId) {
        List<Trip> trips = tripRepository.findByUserId(userId);
        trips.forEach(trip -> trip.getCompanions().size());
        return trips;
    }

}





