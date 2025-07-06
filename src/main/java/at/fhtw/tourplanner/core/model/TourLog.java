package at.fhtw.tourplanner.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourLog {
    private Long id;

    private Tour tour;

    private LocalDate date;

    private String comment;

    private Difficulty difficulty;

    private Rating rating;

    private Double distance;

    private Double duration;
}
