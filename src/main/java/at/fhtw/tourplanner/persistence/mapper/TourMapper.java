package at.fhtw.tourplanner.persistence.mapper;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.persistence.entity.TourEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface TourMapper {

    @Mapping(source = "from", target = "fromLocation")
    @Mapping(source = "to", target = "toLocation")
    @Mapping(source = "distance", target = "distanceInMeters")
    @Mapping(source = "duration", target = "durationInSeconds")
    @Mapping(target = "tourLogs", ignore = true)
    TourEntity toEntity(Tour domain);

    @Mapping(source = "fromLocation", target = "from")
    @Mapping(source = "toLocation", target = "to")
    @Mapping(source = "distanceInMeters", target = "distance")
    @Mapping(source = "durationInSeconds", target = "duration")
    Tour toDomain(TourEntity entity);
}