package at.fhtw.tourplanner.core.model;

import lombok.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    private Long id;

    private String name;

    private String description;

    private Location from;

    private Location to;

    private TransportType transportType;

    private Double distanceInMeters;

    private Double durationInSeconds;

    private List<TourLog> logs;

    public double getAverageDistance() {
        if (logs == null) {
            return 0;
        }

        return logs.stream()
                .mapToDouble(TourLog::getDistanceInMeters)
                .average()
                .orElse(0);
    }

    public double getAverageDuration() {
        return logs.stream()
                .mapToDouble(TourLog::getDurationInSeconds)
                .average()
                .orElse(0);
    }

    public Rating getAverageRating() {
        if (logs.isEmpty()) {
            return Rating.NONE;
        }

        double averageScore = logs.stream()
                .mapToDouble(log -> log.getRating().getScore())
                .average()
                .orElse(0);

        return Arrays.stream(Rating.values())
                .min(Comparator.comparingDouble(rating -> Math.abs(rating.getScore() - averageScore)))
                .orElse(Rating.NONE);
    }
}