package at.fhtw.tourplanner.core.repository;

import at.fhtw.tourplanner.core.model.Location;

import java.util.Optional;

public interface LocationRepository {
    Optional<Location> findById(Long id);

    Location create(Location location);

    Location update(Location location);

    void delete(Long id);

    void deleteAll();
}
