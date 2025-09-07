package at.fhtw.tourplanner.rest.controller;

import at.fhtw.tourplanner.TourPlannerApplication;
import at.fhtw.tourplanner.core.exception.*;
import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TransportType;
import at.fhtw.tourplanner.core.service.TourService;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
@SpringBootTest(classes = {TourPlannerApplication.class})
class TourControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourService tourService;

    @Test
    void getAllTours_shouldReturnList_whenToursExist() throws Exception {
        // arrange
        Tour expectedTour = getTour();
        doReturn(List.of(expectedTour)).when(tourService).getAllTours();

        // act & assert
        MvcResult result = mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse[] allTours = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse[].class);
        assertThat(allTours).hasSize(1);
        assertTourMatches(allTours[0], expectedTour);
    }

    @Test
    void getAllTours_shouldReturnEmptyList_whenNoToursExist() throws Exception {
        // arrange
        doReturn(List.of()).when(tourService).getAllTours();

        // act
        MvcResult result = mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse[] allTours = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse[].class);
        assertThat(allTours).isEmpty();
    }

    @Test
    void getTourById_shouldReturnTour_whenTourExists() throws Exception {
        // arrange
        Tour expectedTour = getTour();
        long tourId = expectedTour.getId();

        doReturn(expectedTour).when(tourService).getTourById(tourId);

        // act
        MvcResult result = mockMvc.perform(get("/api/tours/" + tourId))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse tour = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse.class);
        assertTourMatches(tour, expectedTour);
    }

    @Test
    void getTourById_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        long invalidTourId = 999;

        doThrow(new EntityNotFoundException()).when(tourService).getTourById(invalidTourId);

        // act & assert
        mockMvc.perform(get("/api/tours/" + invalidTourId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTour_shouldReturnCreated_whenRequestIsValid() throws Exception {
        // arrange
        Tour tourToBeCreated = getTour();
        TourCreateRequest createRequest = getTourCreateRequest(tourToBeCreated);

        doReturn(tourToBeCreated).when(tourService).createTour(any(Tour.class));

        // act
        MvcResult result = mockMvc.perform(post("/api/tours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // assert
        TourResponse tour = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse.class);
        assertTourMatches(tour, tourToBeCreated);
    }

    @Test
    void createTour_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        // arrange
        Tour tourToCreate = getTour();
        TourCreateRequest createRequest = getTourCreateRequest(tourToCreate);

        doThrow(new InvalidEntityException()).when(tourService).createTour(any(Tour.class));

        // act & assert
        mockMvc.perform(post("/api/tours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTour_shouldReturnInternalServerError_whenCreateFails() throws Exception {
        // arrange
        Tour tourToCreate = getTour();
        TourCreateRequest createRequest = getTourCreateRequest(tourToCreate);

        doThrow(new EntityCreateException()).when(tourService).createTour(any(Tour.class));

        // act & assert
        mockMvc.perform(post("/api/tours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateTour_shouldReturnUpdatedTour_whenRequestIsValid() throws Exception {
        // arrange
        Tour existingTour = getTour();
        TourUpdateRequest updateRequest = getTourUpdateRequest();

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Tour updatedTour = invocation.getArgument(1);
            updatedTour.setId(id);
            return updatedTour;
        }).when(tourService).updateTour(anyLong(), any(Tour.class));

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
        assertThat(updatedTour.getFrom()).isEqualTo(updateRequest.getFrom());
        assertThat(updatedTour.getTo()).isEqualTo(updateRequest.getTo());
        assertThat(updatedTour.getTransportType()).isEqualTo(updateRequest.getTransportType());
    }

    @Test
    void updateTour_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        Tour existingTour = getTour();
        TourUpdateRequest updateRequest = getTourUpdateRequest(existingTour);

        doThrow(new EntityNotFoundException()).when(tourService).updateTour(anyLong(), any());

        // act & assert
        mockMvc.perform(put("/api/tours/" + existingTour.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTour_shouldReturnInternalServerError_whenUpdateFails() throws Exception {
        // arrange
        Tour existingTour = getTour();
        TourUpdateRequest updateRequest = getTourUpdateRequest(existingTour);

        doThrow(new EntityUpdateException()).when(tourService).updateTour(eq(existingTour.getId()), any(Tour.class));

        // act & assert
        mockMvc.perform(put("/api/tours/" + existingTour.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteTour_shouldReturnNoContent_whenTourExists() throws Exception {
        // arrange
        long tourId = 1L;

        doNothing().when(tourService).deleteTour(tourId);

        // act & assert
        mockMvc.perform(delete("/api/tours/" + tourId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTour_shouldReturnNotFound_whenTourDoesNotExist() throws Exception {
        // arrange
        doThrow(new EntityNotFoundException()).when(tourService).deleteTour(anyLong());

        // act & assert
        mockMvc.perform(delete("/api/tours/" + anyLong()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTour_shouldReturnInternalServerError_whenDeleteFails() throws Exception {
        // arrange
        doThrow(new EntityDeleteException()).when(tourService).deleteTour(anyLong());

        // act & assert
        mockMvc.perform(delete("/api/tours/" + anyLong()))
                .andExpect(status().isInternalServerError());
    }

    private Tour getTour() {
        return Tour.builder()
                .id(1L)
                .name("Nette Wanderung")
                .description("Eine schöne Tour durch die Berge.")
                .from(new Location())
                .to(new Location())
                .transportType(TransportType.HIKE)
                .build();
    }

    private TourCreateRequest getTourCreateRequest(Tour tour)  {
        return TourCreateRequest.builder()
                .name(tour.getName())
                .description(tour.getDescription())
                .from(tour.getFrom())
                .to(tour.getTo())
                .transportType(tour.getTransportType().toString())
                .build();
    }

    private TourUpdateRequest getTourUpdateRequest(Tour tour) {
        return TourUpdateRequest.builder()
                .name(tour.getName())
                .description(tour.getDescription())
                .from(tour.getFrom())
                .to(tour.getTo())
                .transportType(tour.getTransportType().toString())
                .build();
    }

    private TourUpdateRequest getTourUpdateRequest() {
        return TourUpdateRequest.builder()
                .name("Netter Stadtrundgang")
                .description("Eine schöne Tour durch die Stadt.")
                .from(new Location())
                .to(new Location())
                .transportType(TransportType.WALK.toString())
                .build();
    }

    private void assertTourMatches(TourResponse actual, Tour expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getFrom()).isEqualTo(expected.getFrom());
        assertThat(actual.getTo()).isEqualTo(expected.getTo());
        assertThat(TransportType.valueOf(actual.getTransportType())).isEqualTo(expected.getTransportType());
    }
}
