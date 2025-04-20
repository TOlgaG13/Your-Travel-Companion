package com.yourtravelcompanion.your_travel_companion.repositories;

import com.yourtravelcompanion.your_travel_companion.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
