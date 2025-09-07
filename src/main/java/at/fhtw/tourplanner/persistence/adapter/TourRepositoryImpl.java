package at.fhtw.tourplanner.persistence.adapter;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.repository.TourRepository;
import at.fhtw.tourplanner.persistence.entity.TourEntity;
import at.fhtw.tourplanner.persistence.mapper.TourMapper;
import at.fhtw.tourplanner.persistence.repository.JpaTourLogRepository;
import at.fhtw.tourplanner.persistence.repository.JpaTourRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TourRepositoryImpl implements TourRepository {

    private final JpaTourRepository jpaTourRepository;

    private final JpaTourLogRepository jpaTourLogRepository;

    private final TourMapper tourMapper;

    @Override
    public List<Tour> findAll() {
        return jpaTourRepository.findAll().stream()
                .map(tourMapper::toDomain)
                .toList();
    }

    @Override
    public List<Tour> findAllWithLogs() {
        return jpaTourRepository.findAllWithLogs().stream()
                .map(tourMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Tour> findById(Long id) {
        return jpaTourRepository.findById(id)
                .map(tourMapper::toDomain);
    }

    @Override
    public Optional<Tour> findByIdWithLogs(Long id) {
        return jpaTourRepository.findByIdWithLogs(id)
                .map(tourMapper::toDomain);
    }

    @Override
    public Tour create(Tour tour) {
        return save(tour);
    }

    @Override
    public Tour update(Tour tour) {
        return save(tour);
    }

    private Tour save(Tour tour) {
        TourEntity tourEntity = tourMapper.toEntity(tour);
        tourEntity.setTourLogs(jpaTourLogRepository.findByTourId(tour.getId()));
        TourEntity savedTour = jpaTourRepository.save(tourEntity);
        return tourMapper.toDomain(savedTour);
    }

    @Override
    public void delete(Tour tour) {
        TourEntity tourEntity = tourMapper.toEntity(tour);
        jpaTourRepository.delete(tourEntity);
    }

    @Override
    public void deleteAll() {
        jpaTourRepository.deleteAll();
    }
}