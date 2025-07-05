package at.fhtw.tourplanner.persistence.repository;

import at.fhtw.tourplanner.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLocationRepository extends JpaRepository<LocationEntity, Long> {
}
