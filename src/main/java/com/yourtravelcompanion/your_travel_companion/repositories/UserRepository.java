package com.yourtravelcompanion.your_travel_companion.repositories;

import com.yourtravelcompanion.your_travel_companion.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    CustomUser findByEmail(String email);
    CustomUser findByLogin(String login);
}

