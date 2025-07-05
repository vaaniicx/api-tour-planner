package at.fhtw.tourplanner.persistence.repository;

import at.fhtw.tourplanner.persistence.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTourRepository extends JpaRepository<TourEntity, Long> {
}
