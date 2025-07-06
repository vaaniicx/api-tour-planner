package at.fhtw.tourplanner.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteInformation {

    private Double distanceInMeters;

    private Double durationInSeconds;
}
