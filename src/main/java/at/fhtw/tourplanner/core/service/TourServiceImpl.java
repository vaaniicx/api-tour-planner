package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.repository.TourRepository;
import at.fhtw.tourplanner.external.OpenRouteServiceClient;
import at.fhtw.tourplanner.external.dto.RouteInformation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@AllArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    private final LocationService locationService;

    private final OpenRouteServiceClient openRouteServiceClient;

    @Override
    public List<Tour> getAllTours() {
        log.info("Get all tours");

        return tourRepository.findAll();
    }

    @Override
    public Tour getTourById(Long id) {
        log.debug("Get tour with id={}", id);

        return tourRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Tour not found"));
    }

    @Override
    public Tour createTour(Tour tour) {
        log.info("Create new tour: {}", tour.toString());

        if (tour.getId() != null) {
            throw new IllegalArgumentException("New tours must not have an ID.");
        }

        RouteInformation routeInformation = openRouteServiceClient.getRouteInformation(tour);
        tour.setDistance(routeInformation.getDistanceInKilometers());
        tour.setDuration(routeInformation.getDurationInMinutes());

        tour.setFrom(locationService.createLocation(tour.getFrom()));
        tour.setTo(locationService.createLocation(tour.getTo()));

        return tourRepository.create(tour);
    }

    @Override
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
                .distance(routeInformation.getDistanceInKilometers())
                .duration(routeInformation.getDurationInMinutes())
                .build();

        return tourRepository.update(updatedTour);
    }

    @Override
    public void deleteTour(Long id) {
        log.info("Delete tour with id={}", id);

        Tour existingTour = getTourById(id);
        tourRepository.delete(existingTour);
    }
}