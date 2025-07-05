package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.Location;

public interface LocationService {

    Location getLocationById(Long id);

    Location createLocation(Location location);

    Location updateLocation(Location location);

    void deleteLocation(Long id);
}
