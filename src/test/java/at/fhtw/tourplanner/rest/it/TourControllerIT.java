package at.fhtw.tourplanner.rest.it;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TourControllerIT extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        tourRepository.deleteAll();
        tourLogRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void getAllTours_shouldReturnEmptyList_whenNoToursExist() throws Exception {
        // act
        MvcResult result = mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse[] allTours = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse[].class);
        assertThat(allTours).isEmpty();
    }

    @Test
    void getAllTours_shouldReturnList_whenToursExist() throws Exception {
        // arrange
        persistTour(getTour());

        // act
        MvcResult result = mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse[] allTours = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse[].class);
        assertThat(allTours).hasSize(1);
    }

    @Test
    void getTourById_shouldReturnTour_whenTourExist() throws Exception {
        // arrange
        Tour tour = persistTour(getTour());

        // act
        MvcResult result = mockMvc.perform(get("/api/tours/" + tour.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse.class);
        assertTourMatches(actual, tour);
    }

    @Test
    void getTourById_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        long tourId = 999;

        // act & assert
        mockMvc.perform(get("/api/tours/" + tourId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createTour_shouldReturnCreated_whenRequestIsValid() throws Exception {
        // arrange
        Tour tour = getTour();
        TourCreateRequest createRequest = getTourCreateRequest(tour);

        // act
        MvcResult result = mockMvc.perform(post("/api/tours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // assert
        TourResponse createdTour = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse.class);
        assertTourMatches(createdTour, tour);

        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.execute(status -> {
            assertThat(tourRepository.findById(createdTour.getId())).isPresent();
            return null;
        });
    }

    @Test
    void updateTour_shouldReturnUpdatedTour_whenRequestIsValid() throws Exception {
        // arrange
        Tour existingTour = persistTour(getTour());
        TourUpdateRequest updateRequest = getTourUpdateRequest();

        // act
        MvcResult result = mockMvc.perform(put("/api/tours/" + existingTour.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse updatedTour = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse.class);
        assertThat(updatedTour).isNotNull();
        assertThat(updatedTour.getId()).isEqualTo(existingTour.getId());
        assertThat(updatedTour.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedTour.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(updatedTour.getFrom().getLatitude()).isEqualTo(updateRequest.getFrom().getLatitude());
        assertThat(updatedTour.getFrom().getLongitude()).isEqualTo(updateRequest.getFrom().getLongitude());
        assertThat(updatedTour.getTo().getLatitude()).isEqualTo(updateRequest.getTo().getLatitude());
        assertThat(updatedTour.getTo().getLongitude()).isEqualTo(updateRequest.getTo().getLongitude());
        assertThat(updatedTour.getTransportType()).isEqualTo(updateRequest.getTransportType());
    }

    @Test
    void updateTour_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        long tourId = 999;
        TourUpdateRequest updateRequest = getTourUpdateRequest();

        // act & assert
        mockMvc.perform(put("/api/tours/" + tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTour_shouldReturnNoContent_whenTourExists() throws Exception {
        // arrange
        Tour tour = persistTour(getTour());

        // act
        mockMvc.perform(delete("/api/tours/" + tour.getId()))
                .andExpect(status().isNoContent());

        // assert
        assertThat(tourRepository.findById(tour.getId())).isEmpty();
    }

    @Test
    void deleteTour_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        long tourId = 999;

        // act & assert
        mockMvc.perform(delete("/api/tours/" + tourId))
                .andExpect(status().isNotFound());
    }
}
