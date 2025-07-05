package at.fhtw.tourplanner.core.repository;

import at.fhtw.tourplanner.core.model.TourLog;

import java.util.List;
import java.util.Optional;

public interface TourLogRepository {

    Optional<TourLog> findById(Long id);

    List<TourLog> findByTourId(Long id);

    TourLog create(TourLog tourLog);

    TourLog update(TourLog tourLog);

    void delete(Long id);
}
