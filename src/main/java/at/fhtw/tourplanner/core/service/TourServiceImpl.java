package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.repository.TourRepository;
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
        log.info("Create new tour with name={}", tour.getName());

        if (tour.getId() != null) {
            throw new IllegalArgumentException("New tours must not have an ID.");
        }
        return tourRepository.create(tour);
    }

    @Override
    public Tour updateTour(Long tourId, Tour tour) {
        log.info("Update tour with id={}", tourId);

        Tour existingTour = getTourById(tourId);
        Tour updatedTour = new Tour(existingTour.getId(), tour.getName(), tour.getDescription(), tour.getFrom(), tour.getTo());
        return tourRepository.update(updatedTour);
    }

    @Override
    public void deleteTour(Long id) {
        log.info("Delete tour with id={}", id);

        Tour existingTour = getTourById(id);
        tourRepository.delete(existingTour);
    }
}