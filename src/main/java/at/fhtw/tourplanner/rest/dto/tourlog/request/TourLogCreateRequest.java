package at.fhtw.tourplanner.rest.dto.tourlog.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourLogCreateRequest {

    private LocalDate date;

    private String comment;

    private String difficulty;

    private String rating;

    private Double distance;

    private Double duration;
}
