package at.fhtw.tourplanner.persistence.repository;

import at.fhtw.tourplanner.persistence.entity.TourLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTourLogRepository extends JpaRepository<TourLogEntity, Long> {
    List<TourLogEntity> findByTourId(Long id);
}
