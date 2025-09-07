package at.fhtw.tourplanner.rest.it;

import at.fhtw.tourplanner.TourPlannerApplication;
import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TransportType;
import at.fhtw.tourplanner.core.service.TourService;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"integration"})
@SpringBootTest(classes = {TourPlannerApplication.class})
class TourControllerIT {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourService tourService;

    private Tour sampleTour;

    @BeforeEach
    void setUp() throws Exception {
        sampleTour = Tour.builder()
                .id(1L)
                .name("Nette Wanderung")
                .description("Lorem ipsum dolor sit amet, ...")
                .from(new Location())
                .to(new Location())
                .transportType(TransportType.HIKE)
                .build();
    }

    @Test
    void getAllTours_shouldReturnList() throws Exception {
        // arrange
        doReturn(List.of(sampleTour)).when(tourService).getAllTours();

        // act
        MvcResult result = mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andReturn();

        // assert
        TourResponse[] allTours = objectMapper.readValue(result.getResponse().getContentAsString(), TourResponse[].class);
        assertThat(allTours).hasSize(1);

        TourResponse tour = allTours[0];
        assertThat(tour.getId()).isEqualTo(sampleTour.getId());
        assertThat(tour.getName()).isEqualTo(sampleTour.getName());
        assertThat(tour.getDescription()).isEqualTo(sampleTour.getDescription());
        assertThat(tour.getFrom()).isEqualTo(sampleTour.getFrom());
        assertThat(tour.getTo()).isEqualTo(sampleTour.getTo());
        assertThat(TransportType.valueOf(tour.getTransportType())).isEqualTo(sampleTour.getTransportType());
    }
}
