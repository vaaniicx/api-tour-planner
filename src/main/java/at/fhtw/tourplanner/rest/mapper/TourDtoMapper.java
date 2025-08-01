package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocationDtoMapper.class})
public interface TourDtoMapper {

    TourResponse toResponse(Tour tour);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "distance", ignore = true)
    @Mapping(target = "duration", ignore = true)
    Tour fromCreateRequest(TourCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "distance", ignore = true)
    @Mapping(target = "duration", ignore = true)
    Tour fromUpdateRequest(TourUpdateRequest request);
}
