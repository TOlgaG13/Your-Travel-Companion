package com.yourtravelcompanion.your_travel_companion.models;

import jakarta.persistence.*;
import lombok.Data;
import com.yourtravelcompanion.your_travel_companion.models.Companion;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name="trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String description;


    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;//хто створив поїздку

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;//країна поїздки
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Companion> companions;//хто бажає поїхати

    public Trip() {
    }

    public Trip(String destination, LocalDate startDate, LocalDate endDate, String description, LocalDateTime created, CustomUser user, Country country, List<Companion> companions) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.created = created;
        this.user = user;
        this.country = country;
        this.companions = companions;
    }
}
