package at.fhtw.tourplanner.core.repository;

import at.fhtw.tourplanner.core.model.Tour;

import java.util.List;
import java.util.Optional;

public interface TourRepository {
    List<Tour> findAll();

    List<Tour> findAllWithLogs();

    Optional<Tour> findById(Long id);

    Optional<Tour> findByIdWithLogs(Long id);

    Tour create(Tour tour);

    Tour update(Tour tour);

    void delete(Tour tour);

    void deleteAll();
}
