package at.fhtw.tourplanner.core.model;

import lombok.*;

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

    private Double distance;

    private Double duration;
}