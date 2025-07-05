package at.fhtw.tourplanner.persistence.mapper;

import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.persistence.entity.LocationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationEntity toEntity(Location domain);

    Location toDomain(LocationEntity entity);
}
