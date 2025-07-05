package at.fhtw.tourplanner.rest.dto.tour.request;

import at.fhtw.tourplanner.core.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourCreateRequest {

    private String name;

    private String description;

    private Location from;

    private Location to;
}
