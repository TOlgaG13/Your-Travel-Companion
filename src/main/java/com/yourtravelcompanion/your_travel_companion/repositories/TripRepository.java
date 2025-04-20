package com.yourtravelcompanion.your_travel_companion.repositories;


import com.yourtravelcompanion.your_travel_companion.models.Trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByDestinationContainingIgnoreCase(String destination);

    List<Trip> findByCountry_NameContainingIgnoreCase(String country);

    List<Trip> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Trip> findByUserId(Long userId);
    @Query("SELECT t FROM Trip t " +
            "LEFT JOIN FETCH t.companions c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE t.id = :id")
    Optional<Trip> findByIdWithCompanions(@Param("id") Long id);

}

