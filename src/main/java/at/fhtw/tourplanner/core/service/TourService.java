package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.Tour;

import java.util.List;

public interface TourService {
    List<Tour> getAllTours();

    Tour getTourById(Long id);

    Tour createTour(Tour tour);

    Tour updateTour(Long tourId, Tour tour);

    void deleteTour(Long tour);
}
