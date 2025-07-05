package at.fhtw.tourplanner.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private Long id;

    private String latitude;

    private String longitude;
}
