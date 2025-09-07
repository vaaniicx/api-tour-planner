package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TransportType;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TourDtoMapperTest {

    private final TourDtoMapper mapper = Mappers.getMapper(TourDtoMapper.class);

    @Test
    void toResponse_canHandle() {
        // arrange
        Tour tour = getFullTour();

        // act
        TourResponse response = mapper.toResponse(tour);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(tour.getId());
        assertThat(response.getName()).isEqualTo(tour.getName());
        assertThat(response.getDescription()).isEqualTo(tour.getDescription());
        assertThat(response.getFrom()).isEqualTo(tour.getFrom());
        assertThat(response.getTo()).isEqualTo(tour.getTo());
        assertThat(TransportType.valueOf(response.getTransportType())).isEqualTo(tour.getTransportType());
    }

    @Test
    void toResponse_canHandleNull() {
        assertDoesNotThrow(() -> mapper.toResponse(null));
    }

    @Test
    void toResponse_canHandleEmpty() {
        assertDoesNotThrow(() -> mapper.toResponse(new Tour()));
    }

    @Test
    void fromCreateRequest_canHandle() {
        // arrange
        TourCreateRequest request = getFullTourCreateRequest();

        // act
        Tour tour = mapper.fromCreateRequest(request);

        // assert
        assertThat(tour).isNotNull();
        assertThat(tour.getFrom()).isEqualTo(request.getFrom());
        assertThat(tour.getTo()).isEqualTo(request.getTo());
        assertThat(tour.getTransportType()).isEqualTo(TransportType.valueOf(request.getTransportType()));

        assertThat(tour.getId()).isNull();
        assertThat(tour.getDistanceInMeters()).isNull();
        assertThat(tour.getDurationInSeconds()).isNull();
        assertThat(tour.getLogs()).isNull();
    }

    @Test
    void fromCreateRequest_canHandleNull() {
        assertDoesNotThrow(() -> mapper.fromCreateRequest(null));
    }

    @Test
    void fromCreateRequest_canHandleEmpty() {
        assertDoesNotThrow(() -> mapper.fromCreateRequest(new TourCreateRequest()));
    }

    @Test
    void fromUpdateRequest_canHandle() {
        // arrange
        TourUpdateRequest request = getFullTourUpdateRequest();

        // act
        Tour tour = mapper.fromUpdateRequest(request);

        // assert
        assertThat(tour).isNotNull();
        assertThat(tour.getName()).isEqualTo(request.getName());
        assertThat(tour.getDescription()).isEqualTo(request.getDescription());
        assertThat(tour.getFrom()).isEqualTo(request.getFrom());
        assertThat(tour.getTo()).isEqualTo(request.getTo());
        assertThat(tour.getTransportType()).isEqualTo(TransportType.valueOf(request.getTransportType()));

        assertThat(tour.getId()).isNull();
        assertThat(tour.getDistanceInMeters()).isNull();
        assertThat(tour.getDurationInSeconds()).isNull();
        assertThat(tour.getLogs()).isNull();
    }

    @Test
    void fromUpdateRequest_canHandleNull() {
        assertDoesNotThrow(() -> mapper.fromUpdateRequest(null));
    }

    @Test
    void fromUpdateRequest_canHandleEmpty() {
        assertDoesNotThrow(() -> mapper.fromUpdateRequest(null));
    }

    private Tour getFullTour() {
        return Tour.builder()
                .name("name")
                .description("description")
                .from(new Location(null, "from-lat", "from-lng"))
                .to(new Location(null, "to-lat", "to-lng"))
                .transportType(TransportType.WALK)
                .distanceInMeters(1000d)
                .durationInSeconds(600d)
                .build();
    }

    private TourUpdateRequest getFullTourUpdateRequest() {
        return TourUpdateRequest.builder()
                .name("name")
                .description("description")
                .from(new Location(null, "from-lat", "from-lng"))
                .to(new Location(null, "to-lat", "to-lng"))
                .transportType(TransportType.HIKE.toString())
                .build();
    }

    private TourCreateRequest getFullTourCreateRequest() {
        return TourCreateRequest.builder()
                .name("name")
                .description("description")
                .from(new Location(null, "from-lat", "from-lng"))
                .to(new Location(null, "to-lat", "to-lng"))
                .transportType(TransportType.HIKE.toString())
                .build();
    }
}
