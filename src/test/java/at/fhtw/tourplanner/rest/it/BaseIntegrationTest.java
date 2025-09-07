package at.fhtw.tourplanner.rest.it;

import at.fhtw.tourplanner.TourPlannerApplication;
import at.fhtw.tourplanner.core.model.*;
import at.fhtw.tourplanner.core.repository.LocationRepository;
import at.fhtw.tourplanner.core.repository.TourLogRepository;
import at.fhtw.tourplanner.core.repository.TourRepository;
import at.fhtw.tourplanner.external.OpenRouteServiceClient;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"integration"})
@SpringBootTest(classes = {TourPlannerApplication.class})
public class BaseIntegrationTest {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TourRepository tourRepository;

    @Autowired
    protected TourLogRepository tourLogRepository;

    @Autowired
    protected LocationRepository locationRepository;

    @Autowired
    protected OpenRouteServiceClient openRouteServiceClient;

    @Autowired
    protected PlatformTransactionManager transactionManager;


    protected Tour getTour() {
        return Tour.builder()
                .name("Nette Wanderung")
                .description("Eine schöne Tour durch die Berge.")
                .from(new Location(null, "48.20094760114545", "16.4730978012085"))
                .to(new Location(null, "48.19584511902266", "16.47883772850037"))
                .transportType(TransportType.HIKE)
                .build();
    }

    protected TourLog getTourLog(Tour tour) {
        return TourLog.builder()
                .tour(tour)
                .date(LocalDate.now())
                .comment("Das war eine tolle Wanderung.")
                .difficulty(Difficulty.MEDIUM)
                .rating(Rating.PERFECT)
                .distanceInMeters(null)
                .durationInSeconds(null)
                .build();
    }

    protected TourCreateRequest getTourCreateRequest(Tour tour)  {
        return TourCreateRequest.builder()
                .name(tour.getName())
                .description(tour.getDescription())
                .from(tour.getFrom())
                .to(tour.getTo())
                .transportType(tour.getTransportType().toString())
                .build();
    }

    protected TourUpdateRequest getTourUpdateRequest() {
        return TourUpdateRequest.builder()
                .name("Netter Stadtrundgang")
                .description("Eine schöne Tour durch die Stadt.")
                .from(new Location(null, "48.20094760114545", "16.4730978012085"))
                .to(new Location(null, "48.19584511902266", "16.47883772850037"))
                .transportType(TransportType.WALK.toString())
                .build();
    }

    protected TourLogCreateRequest getTourLogCreateRequest(TourLog tourLog)  {
        return TourLogCreateRequest.builder()
                .date(tourLog.getDate())
                .comment(tourLog.getComment())
                .difficulty(tourLog.getDifficulty().toString())
                .rating(tourLog.getRating().toString())
                .distance(tourLog.getDistanceInMeters())
                .duration(tourLog.getDurationInSeconds())
                .build();
    }

    protected TourLogUpdateRequest getTourLogUpdateRequest() {
        return TourLogUpdateRequest.builder()
                .date(LocalDate.now())
                .comment("War eine angenehme Tour.")
                .difficulty(Difficulty.EASY.toString())
                .rating(Rating.GOOD.toString())
                .distance(null)
                .duration(null)
                .build();
    }

    protected Tour persistTour(Tour tour) {
        tour.setFrom(locationRepository.create(tour.getFrom()));
        tour.setTo(locationRepository.create(tour.getTo()));
        return tourRepository.create(tour);
    }

    protected TourLog persistTourLog(TourLog tourLog) {
        return tourLogRepository.create(tourLog);
    }

    protected void assertTourMatches(TourResponse actual, Tour expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getFrom().getLatitude()).isEqualTo(expected.getFrom().getLatitude());
        assertThat(actual.getFrom().getLongitude()).isEqualTo(expected.getFrom().getLongitude());
        assertThat(actual.getTo().getLatitude()).isEqualTo(expected.getTo().getLatitude());
        assertThat(actual.getTo().getLongitude()).isEqualTo(expected.getTo().getLongitude());
        assertThat(TransportType.valueOf(actual.getTransportType())).isEqualTo(expected.getTransportType());
    }

    protected void assertTourLogMatches(TourLogResponse actual, TourLog expected) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getDate()).isEqualTo(expected.getDate());
        assertThat(actual.getComment()).isEqualTo(expected.getComment());
        assertThat(Difficulty.valueOf(actual.getDifficulty())).isEqualTo(expected.getDifficulty());
        assertThat(Rating.valueOf(actual.getRating())).isEqualTo(expected.getRating());
        assertThat(actual.getDistance()).isEqualTo(expected.getDistanceInMeters());
        assertThat(actual.getDuration()).isEqualTo(expected.getDurationInSeconds());
    }
}
