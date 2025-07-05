package at.fhtw.tourplanner.persistence.adapter;

import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.core.repository.TourLogRepository;
import at.fhtw.tourplanner.persistence.entity.TourEntity;
import at.fhtw.tourplanner.persistence.entity.TourLogEntity;
import at.fhtw.tourplanner.persistence.mapper.TourLogMapper;
import at.fhtw.tourplanner.persistence.repository.JpaTourLogRepository;
import at.fhtw.tourplanner.persistence.repository.JpaTourRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TourLogRepositoryImpl implements TourLogRepository {

    private final JpaTourLogRepository jpaTourLogRepository;

    private final JpaTourRepository jpaTourRepository;

    private final TourLogMapper tourLogMapper;

    @Override
    public Optional<TourLog> findById(Long id) {
        return jpaTourLogRepository.findById(id)
                .map(tourLogMapper::toDomain);
    }

    @Override
    public List<TourLog> findByTourId(Long id) {
        return jpaTourLogRepository.findByTourId(id)
                .stream().map(tourLogMapper::toDomain)
                .toList();
    }

    @Override
    public TourLog create(TourLog tourLog) {
        return save(tourLog);
    }

    @Override
    public TourLog update(TourLog tourLog) {
        return save(tourLog);
    }

    private TourLog save(TourLog tourLog) {
        TourLogEntity tourLogEntity = tourLogMapper.toEntity(tourLog);
        jpaTourRepository.findById(tourLog.getTour().getId()).ifPresent(tourLogEntity::setTour);

        TourLogEntity savedEntity = jpaTourLogRepository.save(tourLogEntity);
        return tourLogMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Long id) {
        jpaTourLogRepository.deleteById(id);
    }
}