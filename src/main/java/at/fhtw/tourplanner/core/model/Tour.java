package at.fhtw.tourplanner.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    private Long id;

    private String name;

    private String description;

    private Location from;

    private Location to;
}