package at.fhtw.tourplanner.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLog {
    private Long id;

    private Tour tour;

    private LocalDate date;

    private String comment;

    // difficulty
    // total distance
    // total time
    // rating
}
