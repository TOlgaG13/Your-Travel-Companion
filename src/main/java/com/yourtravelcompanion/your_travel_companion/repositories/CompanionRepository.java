package com.yourtravelcompanion.your_travel_companion.repositories;

import com.yourtravelcompanion.your_travel_companion.models.Companion;
import com.yourtravelcompanion.your_travel_companion.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanionRepository extends JpaRepository<Companion, Long> {
    List<Companion> findByTripId(Long tripId);
    List<Companion> findByUserId(Long userId);
    @Query("SELECT c.trip FROM Companion c WHERE c.user.id = :userId AND c.trip.user.id <> :userId")
    List<Trip> findTripsWhereUserIsOnlyCompanion(@Param("userId") Long userId);

    void deleteAllByUserId(Long userId);


}
