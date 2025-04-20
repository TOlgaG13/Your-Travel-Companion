package com.yourtravelcompanion.your_travel_companion.services;
import com.yourtravelcompanion.your_travel_companion.models.Companion;
import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import com.yourtravelcompanion.your_travel_companion.models.Trip;
import com.yourtravelcompanion.your_travel_companion.repositories.CompanionRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.TripRepository;
import com.yourtravelcompanion.your_travel_companion.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CompanionService {
    private final CompanionRepository companionRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public CompanionService(CompanionRepository companionRepository,
                            TripRepository tripRepository,
                            UserRepository userRepository) {
        this.companionRepository = companionRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Companion> getCompanionsByUserId(Long userId) {
        return companionRepository.findByUserId(userId);
    }

    @Transactional
    public void joinTrip(Long tripId, Long userId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));
        CustomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Перевіряємо, чи юсер вже подавав заявку
        boolean alreadyJoined = companionRepository.findByTripId(tripId).stream()
                .anyMatch(companion -> userId.equals(companion.getUser().getId()));

        if (alreadyJoined) {
            return;
        }

        Companion companion = new Companion();
        companion.setTrip(trip);
        companion.setUser(user);

        companion.setCreatedAt(LocalDateTime.now());
        companionRepository.save(companion);
    }

    @Transactional
    public void leaveTrip(Long tripId, Long userId) {
        System.out.println("Leave request from userId=" + userId + " for tripId=" + tripId);

       Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));

       Companion companion = companionRepository.findByTripId(tripId).stream()
                .filter(c -> userId != null && userId.equals(c.getUser().getId()))
                .findFirst()
                .orElse(null);

        if (companion != null) {
            //видаляємо з колекції trip.getCompanions()
            trip.getCompanions().remove(companion);

            //потім видаляється з репозиторію
            companionRepository.delete(companion);

            System.out.println("Видаляємо клмпаньона : " + companion.getId());
        } else {
            System.out.println("Команьона не знайдено");
        }
    }



    @Transactional
    public void deleteCompanion(Long id) {
        companionRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Optional<Companion> getById(Long id) {
        return companionRepository.findById(id);
    }




}

