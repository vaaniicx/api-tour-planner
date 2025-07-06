package at.fhtw.tourplanner.core.service;

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
        return locationRepository.create(location);
    }

    public Location updateLocation(Location location) {
        return locationRepository.update(location);
    }
}
