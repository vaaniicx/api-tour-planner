package at.fhtw.tourplanner.persistence.mapper;

import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.persistence.entity.TourLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourLogMapper {

    TourLogEntity toEntity(TourLog domain);

    @Mapping(target = "tour", ignore = true)
    TourLog toDomain(TourLogEntity entity);
}

