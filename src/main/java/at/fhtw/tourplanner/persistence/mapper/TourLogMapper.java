package at.fhtw.tourplanner.persistence.mapper;

import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.persistence.entity.TourLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TourMapper.class})
public interface TourLogMapper {

    TourLogEntity toEntity(TourLog domain);

    TourLog toDomain(TourLogEntity entity);
}

