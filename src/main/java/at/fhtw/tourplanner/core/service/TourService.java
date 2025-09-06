package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.repository.TourLogRepository;
import at.fhtw.tourplanner.core.repository.TourRepository;
import at.fhtw.tourplanner.external.OpenRouteServiceClient;
import at.fhtw.tourplanner.external.RouteInformation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@AllArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    private final TourLogRepository tourLogRepository;

    private final LocationService locationService;

    private final OpenRouteServiceClient openRouteServiceClient;

    public List<Tour> getAllTours() {
        log.info("Get all tours");
        return tourRepository.findAll();
    }

    public List<Tour> getAllToursWithLogs() {
        log.debug("Get all tours with logs");
        return tourRepository.findAllWithLogs();
    }

    public Tour getTourById(Long id) {
        log.debug("Get tour with id={}", id);
        return tourRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tour not found"));
    }

    public Tour getTourWithLogs(Long id) {
        log.debug("Get tour with logs");
        return tourRepository.findByIdWithLogs(id)
                .orElseThrow(() -> new NoSuchElementException("Tour not found"));
    }

    public Tour createTour(Tour tour) {
        log.info("Create new tour: {}", tour.toString());

        if (tour.getId() != null) {
            throw new IllegalArgumentException("New tours must not have an ID.");
        }

        RouteInformation routeInformation = openRouteServiceClient.getRouteInformation(tour);
        tour.setDistanceInMeters(routeInformation.getDistanceInMeters());
        tour.setDurationInSeconds(routeInformation.getDurationInSeconds());

        tour.setFrom(locationService.createLocation(tour.getFrom()));
        tour.setTo(locationService.createLocation(tour.getTo()));

        return tourRepository.create(tour);
    }

    public Tour updateTour(Long tourId, Tour tour) {
        log.info("Update tour with id={}", tourId);

        Tour existingTour = getTourById(tourId);

        RouteInformation routeInformation = openRouteServiceClient.getRouteInformation(tour);

        Tour updatedTour = Tour.builder()
                .id(existingTour.getId())
                .name(tour.getName())
                .description(tour.getDescription())
                .from(locationService.updateLocation(tour.getFrom()))
                .to(locationService.updateLocation(tour.getTo()))
                .transportType(tour.getTransportType())
                .distanceInMeters(routeInformation.getDistanceInMeters())
                .durationInSeconds(routeInformation.getDurationInSeconds())
                .build();

        return tourRepository.update(updatedTour);
    }

    public void deleteTour(Long id) {
        log.info("Delete tour with id={}", id);

        Tour existingTour = getTourById(id);
        tourRepository.delete(existingTour);
    }
}