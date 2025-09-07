package at.fhtw.tourplanner.rest.it;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TourLogControllerIT extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        tourRepository.deleteAll();
        tourLogRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void getAllTourLogs_shouldReturnList_whenTourLogsExist() throws Exception {
        // arrange
        Tour tour = persistTour(getTour());
        TourLog expectedTourLog = persistTourLog(getTourLog(tour));

        // act
        MvcResult result = mockMvc.perform(get("/api/tours/"+ tour.getId() + "/logs"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourLogResponse[] allTourLogs = objectMapper.readValue(result.getResponse().getContentAsString(), TourLogResponse[].class);
        assertThat(allTourLogs).hasSize(1);
        assertTourLogMatches(allTourLogs[0], expectedTourLog);
    }

    @Test
    void getAllTourLogs_shouldReturnEmptyList_whenNoTourLogsExist() throws Exception {
        // arrange
        long tourId = 999;

        // act
        MvcResult result = mockMvc.perform(get("/api/tours/"+ tourId + "/logs"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourLogResponse[] allTourLogs = objectMapper.readValue(result.getResponse().getContentAsString(), TourLogResponse[].class);
        assertThat(allTourLogs).isEmpty();
    }

    @Test
    void createTourLog_shouldReturnCreated_whenRequestIsValid() throws Exception {
        // arrange
        Tour tour = persistTour(getTour());
        TourLog tourLog = getTourLog(tour);
        TourLogCreateRequest createRequest = getTourLogCreateRequest(tourLog);

        // act
        MvcResult result = mockMvc.perform(post("/api/tours/"+ tour.getId() + "/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // assert
        TourLogResponse createdTourLog = objectMapper.readValue(result.getResponse().getContentAsString(), TourLogResponse.class);
        assertTourLogMatches(createdTourLog, tourLog);

        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.execute(status -> {
            assertThat(tourLogRepository.findById(createdTourLog.getId())).isPresent();
            return null;
        });
    }

    @Test
    void createTourLog_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        long tourId = 999;
        TourLogCreateRequest createRequest = getTourLogCreateRequest(getTourLog(getTour()));

        // act
        mockMvc.perform(post("/api/tours/"+ tourId + "/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTourLog_shouldReturnUpdatedTour_whenRequestIsValid() throws Exception {
        // arrange
        Tour tour = persistTour(getTour());
        TourLog existingTourLog = persistTourLog(getTourLog(tour));
        TourLogUpdateRequest updateRequest = getTourLogUpdateRequest();

        // act
        MvcResult result = mockMvc.perform(put("/api/tours/"+ tour.getId() + "/logs/" + existingTourLog.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourLogResponse updatedTourLog = objectMapper.readValue(result.getResponse().getContentAsString(), TourLogResponse.class);
        Assertions.assertThat(updatedTourLog).isNotNull();
        Assertions.assertThat(updatedTourLog.getId()).isEqualTo(existingTourLog.getId());
        Assertions.assertThat(updatedTourLog.getDate()).isEqualTo(updateRequest.getDate());
        Assertions.assertThat(updatedTourLog.getComment()).isEqualTo(updateRequest.getComment());
        Assertions.assertThat(updatedTourLog.getDifficulty()).isEqualTo(updateRequest.getDifficulty());
        Assertions.assertThat(updatedTourLog.getRating()).isEqualTo(updateRequest.getRating());
        Assertions.assertThat(updatedTourLog.getDistance()).isEqualTo(updateRequest.getDistance());
        Assertions.assertThat(updatedTourLog.getDuration()).isEqualTo(updateRequest.getDuration());
    }

    @Test
    void updateTourLog_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        long tourId = 999;
        long tourLogId = 1;

        TourLogUpdateRequest updateRequest = getTourLogUpdateRequest();

        // act & assert
        mockMvc.perform(put("/api/tours/"+ tourId + "/logs/" + tourLogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTour_shouldReturnNoContent_whenTourExists() throws Exception {
        // arrange
        Tour tour = persistTour(getTour());
        TourLog tourLog = persistTourLog(getTourLog(tour));

        // act
        mockMvc.perform(delete("/api/tours/"+ tour.getId() + "/logs/" + tourLog.getId()))
                .andExpect(status().isNoContent());

        // assert
        assertThat(tourLogRepository.findById(tourLog.getId())).isEmpty();
    }

    @Test
    void deleteTour_shouldReturnNotFound_whenTourLogDoesNotExist() throws Exception {
        // arrange
        long tourId = 999;
        long tourLogId = 1;

        // act & assert
        mockMvc.perform(delete("/api/tours/"+ tourId + "/logs/" + tourLogId))
                .andExpect(status().isNotFound());
    }
}
