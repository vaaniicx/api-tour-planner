package at.fhtw.tourplanner.rest.dto.tour.response;

import at.fhtw.tourplanner.core.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourResponse {

    public Long id;

    public String name;

    public String description;

    public Location from;

    public Location to;
}
