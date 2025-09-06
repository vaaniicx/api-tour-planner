package at.fhtw.tourplanner.persistence.mapper;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.persistence.entity.TourEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { LocationMapper.class, TourLogMapper.class })
public interface TourMapper {

    @Mapping(target = "fromLocation", source = "from")
    @Mapping(target = "toLocation", source = "to")
    @Mapping(target = "tourLogs", ignore = true)
    TourEntity toEntity(Tour domain);

    @Mapping(target = "from", source = "fromLocation")
    @Mapping(target = "to", source = "toLocation")
    @Mapping(target = "logs", source = "tourLogs")
    Tour  toDomain(TourEntity entity);
}