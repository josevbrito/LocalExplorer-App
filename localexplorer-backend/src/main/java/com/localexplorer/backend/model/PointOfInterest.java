package com.localexplorer.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class PointOfInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Strategy to auto-increment the ID in the database
    private Long id;

    private String name;
    private String description;
    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING) // Annotation to specify that the enum should be stored as a string in the database
    private PointOfInterestType type; // Type of point of interest, e.g., RESTAURANT, PARK, etc.

}