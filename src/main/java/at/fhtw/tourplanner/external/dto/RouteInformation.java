package at.fhtw.tourplanner.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteInformation {

    private Double distanceInMeters;

    private Double durationInSeconds;

    public Double getDistanceInKilometers() {
        return distanceInMeters / 1000;
    }

    public Double getDurationInMinutes() {
        return durationInSeconds / 60;
    }
}
