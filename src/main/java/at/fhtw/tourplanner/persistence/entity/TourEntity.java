package at.fhtw.tourplanner.persistence.entity;

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

    private String fromLocation;

    private String toLocation;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TourLogEntity> tourLogs = new ArrayList<>();

    // private ?? (String/Enum) transportType;
    // private ?? estimatedTimeInMinutes; -> retrieved from OpenRouteService.org
    // private ?? routeInformation;
}