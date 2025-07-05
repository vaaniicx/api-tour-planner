package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourLogDtoMapper {

    @Mapping(target = "tourId", source = "tour.id")
    TourLogResponse toResponse(TourLog tourLog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tour", ignore = true)
    TourLog fromCreateRequest(TourLogCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tour", ignore = true)
    TourLog fromUpdateRequest(TourLogUpdateRequest request);
}
