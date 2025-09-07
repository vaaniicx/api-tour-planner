package at.fhtw.tourplanner.persistence.adapter;

import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.repository.LocationRepository;
import at.fhtw.tourplanner.persistence.entity.LocationEntity;
import at.fhtw.tourplanner.persistence.mapper.LocationMapper;
import at.fhtw.tourplanner.persistence.repository.JpaLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final JpaLocationRepository jpaLocationRepository;

    private final LocationMapper locationMapper;

    @Override
    public Optional<Location> findById(Long id) {
        return jpaLocationRepository.findById(id)
                .map(locationMapper::toDomain);
    }

    @Override
    public Location create(Location location) {
        return save(location);
    }

    @Override
    public Location update(Location location) {
        return save(location);
    }

    private Location save(Location location) {
        LocationEntity locationEntity = locationMapper.toEntity(location);
        LocationEntity savedEntity = jpaLocationRepository.save(locationEntity);
        return locationMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Long id) {
        jpaLocationRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        jpaLocationRepository.deleteAll();
    }
}
