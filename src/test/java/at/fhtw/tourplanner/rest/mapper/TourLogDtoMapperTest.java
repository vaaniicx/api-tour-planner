package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.Difficulty;
import at.fhtw.tourplanner.core.model.Rating;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TourLogDtoMapperTest {

    private final TourLogDtoMapper mapper = Mappers.getMapper(TourLogDtoMapper.class);

    @Test
    void toResponse_canHandle() {
        // arrange
        TourLog tourLog = getFullTourLog();

        // act
        TourLogResponse response = mapper.toResponse(tourLog);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(tourLog.getId());
        assertThat(response.getDate()).isEqualTo(tourLog.getDate());
        assertThat(response.getComment()).isEqualTo(tourLog.getComment());
        assertThat(Difficulty.valueOf(response.getDifficulty())).isEqualTo(tourLog.getDifficulty());
        assertThat(Rating.valueOf(response.getRating())).isEqualTo(tourLog.getRating());
        assertThat(response.getDistance()).isEqualTo(tourLog.getDistanceInMeters());
        assertThat(response.getDuration()).isEqualTo(tourLog.getDurationInSeconds());
    }

    @Test
    void toResponse_canHandleNull() {
        assertDoesNotThrow(() -> mapper.toResponse(null));
    }

    @Test
    void toResponse_canHandleEmpty() {
        assertDoesNotThrow(() -> mapper.toResponse(new TourLog()));
    }

    @Test
    void fromCreateRequest_canHandle() {
        // arrange
        TourLogCreateRequest request = getFullTourLogCreateRequest();

        // act
        TourLog tourLog = mapper.fromCreateRequest(request);

        // assert
        assertThat(tourLog).isNotNull();
        assertThat(tourLog.getDate()).isEqualTo(request.getDate());
        assertThat(tourLog.getComment()).isEqualTo(request.getComment());
        assertThat(tourLog.getDifficulty()).isEqualTo(Difficulty.valueOf(request.getDifficulty()));
        assertThat(tourLog.getRating()).isEqualTo(Rating.valueOf(request.getRating()));
        assertThat(tourLog.getDistanceInMeters()).isEqualTo(request.getDistance());
        assertThat(tourLog.getDurationInSeconds()).isEqualTo(request.getDuration());

        assertThat(tourLog.getId()).isNull();
        assertThat(tourLog.getTour()).isNull();
    }

    @Test
    void fromCreateRequest_canHandleNull() {
        assertDoesNotThrow(() -> mapper.fromCreateRequest(null));
    }

    @Test
    void fromCreateRequest_canHandleEmpty() {
        assertDoesNotThrow(() -> mapper.fromCreateRequest(new TourLogCreateRequest()));
    }

    @Test
    void fromUpdateRequest_canHandle() {
        // arrange
        TourLogUpdateRequest request = getFullTourLogUpdateRequest();

        // act
        TourLog tourLog = mapper.fromUpdateRequest(request);

        // assert
        assertThat(tourLog).isNotNull();
        assertThat(tourLog.getDate()).isEqualTo(request.getDate());
        assertThat(tourLog.getComment()).isEqualTo(request.getComment());
        assertThat(tourLog.getDifficulty()).isEqualTo(Difficulty.valueOf(request.getDifficulty()));
        assertThat(tourLog.getRating()).isEqualTo(Rating.valueOf(request.getRating()));
        assertThat(tourLog.getDistanceInMeters()).isEqualTo(request.getDistance());
        assertThat(tourLog.getDurationInSeconds()).isEqualTo(request.getDuration());

        assertThat(tourLog.getId()).isNull();
        assertThat(tourLog.getTour()).isNull();
    }

    @Test
    void fromUpdateRequest_canHandleNull() {
        assertDoesNotThrow(() -> mapper.fromUpdateRequest(null));
    }

    @Test
    void fromUpdateRequest_canHandleEmpty() {
        assertDoesNotThrow(() -> mapper.fromUpdateRequest(new TourLogUpdateRequest()));
    }

    private TourLog getFullTourLog() {
        return TourLog.builder()
                .id(1L)
                .tour(new Tour())
                .date(LocalDate.now())
                .comment("comment")
                .difficulty(Difficulty.MEDIUM)
                .rating(Rating.PERFECT)
                .distanceInMeters(1000d)
                .durationInSeconds(600d)
                .build();
    }

    private TourLogCreateRequest getFullTourLogCreateRequest() {
        return TourLogCreateRequest.builder()
                .date(LocalDate.now())
                .comment("comment")
                .difficulty(Difficulty.EASY.toString())
                .rating(Rating.PERFECT.toString())
                .distance(1000d)
                .duration(600d)
                .build();
    }

    private TourLogUpdateRequest getFullTourLogUpdateRequest() {
        return TourLogUpdateRequest.builder()
                .date(LocalDate.now())
                .comment("comment")
                .difficulty(Difficulty.EASY.toString())
                .rating(Rating.PERFECT.toString())
                .distance(1000d)
                .duration(600d)
                .build();
    }
}
