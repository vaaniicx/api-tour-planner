package at.fhtw.tourplanner.persistence.mapper;

import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.persistence.entity.TourLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TourMapper.class})
public interface TourLogMapper {

    @Mapping(target = "distanceInMeters", source = "distance")
    @Mapping(target = "durationInSeconds", source = "duration")
    TourLogEntity toEntity(TourLog domain);

    @Mapping(target = "distance", source = "distanceInMeters")
    @Mapping(target = "duration", source = "durationInSeconds")
    TourLog toDomain(TourLogEntity entity);
}

