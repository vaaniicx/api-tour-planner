package at.fhtw.tourplanner.persistence.entity;

import at.fhtw.tourplanner.core.model.Difficulty;
import at.fhtw.tourplanner.core.model.Rating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tour_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tour_id", nullable = false)
    private TourEntity tour;

    private String comment;

    private Difficulty difficulty;

    private Rating rating;

    private Double distanceInMeters;

    private Double durationInSeconds;

    private LocalDate date;
}
