package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.rest.dto.tour.LocationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationDtoMapper {

    LocationDto toDto(Location model);

    Location fromDto(LocationDto dto);
}
