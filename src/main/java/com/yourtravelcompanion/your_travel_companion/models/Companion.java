package com.yourtravelcompanion.your_travel_companion.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "companions")
public class Companion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //юсер який подав заявку
    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;

    // поїздка, на яку подається
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;


    private LocalDateTime createdAt;

    public Companion() {
    }

    public Companion(CustomUser user, Trip trip, LocalDateTime createdAt) {
        this.user = user;
        this.trip = trip;

        this.createdAt = createdAt;
    }
}

