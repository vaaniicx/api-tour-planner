package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.exception.*;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.repository.TourRepository;
import at.fhtw.tourplanner.external.OpenRouteServiceClient;
import at.fhtw.tourplanner.external.RouteInformation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    private final LocationService locationService;

    private final OpenRouteServiceClient openRouteServiceClient;

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Tour> getAllToursWithLogs() {
        return tourRepository.findAllWithLogs();
    }

    public Tour getTourById(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find tour with id: " + id));
    }

    public Tour getTourWithLogs(Long id) {
        return tourRepository.findByIdWithLogs(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find tour with id: " + id));
    }

    public Tour createTour(Tour tour) {
        if (tour.getId() != null) {
            throw new InvalidEntityException("New tours must not have an id.");
        }

        try {
            RouteInformation routeInformation = openRouteServiceClient.getRouteInformation(tour);
            tour.setDistanceInMeters(routeInformation.getDistanceInMeters());
            tour.setDurationInSeconds(routeInformation.getDurationInSeconds());

            tour.setFrom(locationService.createLocation(tour.getFrom()));
            tour.setTo(locationService.createLocation(tour.getTo()));
            return tourRepository.create(tour);
        } catch (Exception e) {
            throw new EntityCreateException("Could not create tour: ", e);
        }
    }

    public Tour updateTour(Long tourId, Tour tour) {
        Tour existingTour = getTourById(tourId);

        try {
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
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update tour with id: " + tourId, e);
        }
    }

    public void deleteTour(Long id) {
        Tour existingTour = getTourById(id);

        try {
            tourRepository.delete(existingTour);
        } catch (Exception e) {
            throw new EntityDeleteException("Could not delete tour with id: " + id, e);
        }
    }
}