package at.fhtw.tourplanner.rest.dto.tour.response;

import at.fhtw.tourplanner.core.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse {

    private Long id;

    private String name;

    private String description;

    private Location from;

    private Location to;

    private String transportType;

    private Double distance;

    private Double duration;
}
