package at.fhtw.tourplanner.rest.dto.tourlog.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLogResponse {

    private Long id;

    private Long tourId;

    private LocalDate date;

    private String comment;

    private String difficulty;

    private String rating;

    private Double distance;

    private Double duration;
}
