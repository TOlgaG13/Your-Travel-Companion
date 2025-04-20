package com.yourtravelcompanion.your_travel_companion.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Trip> trips;

    public Country() {}

    public Country(String name, List<Trip> trips) {
        this.name = name;
        this.trips = trips;
    }

}

