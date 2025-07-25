package at.fhtw.tourplanner.rest.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    
    private Long id;

    private String latitude;

    private String longitude;
}
