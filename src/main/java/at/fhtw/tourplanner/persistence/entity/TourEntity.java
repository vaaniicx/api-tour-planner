package at.fhtw.tourplanner.persistence.entity;

import at.fhtw.tourplanner.core.model.TransportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private LocationEntity fromLocation;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private LocationEntity toLocation;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TourLogEntity> tourLogs = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    private Double distanceInMeters;

    private Double durationInSeconds;
}