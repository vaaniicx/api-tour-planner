package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.exception.EntityCreateException;
import at.fhtw.tourplanner.core.exception.EntityUpdateException;
import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.repository.LocationRepository;
import at.fhtw.tourplanner.persistence.adapter.LocationRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createLocation(Location location) {
        try {
            return locationRepository.create(location);
        } catch (Exception e) {
            throw new EntityCreateException("Could not create entity: ", e);
        }
    }

    public Location updateLocation(Location location) {
        try {
            return locationRepository.update(location);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update entity: ", e);
        }
    }
}
