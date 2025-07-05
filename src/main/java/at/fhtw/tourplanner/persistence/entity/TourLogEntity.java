package at.fhtw.tourplanner.persistence.entity;

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

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_id", nullable = false)
    private TourEntity tour;

    private String comment;

    // difficulty
    // total distance
    // total time
    // rating

    private LocalDate date;
}
