package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.repository.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location getLocationById(Long id) {
        log.info("Get location by id={}", id);
        return locationRepository.findById(id).orElse(null);
    }

    @Override
    public Location createLocation(Location location) {
        return locationRepository.create(location);
    }

    @Override
    public Location updateLocation(Location location) {
        return locationRepository.update(location);
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.delete(id);
    }
}
