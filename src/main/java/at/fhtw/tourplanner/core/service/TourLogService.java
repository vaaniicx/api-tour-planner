package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.exception.*;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.core.repository.TourLogRepository;
import at.fhtw.tourplanner.core.repository.TourRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@AllArgsConstructor
public class TourLogService {

    private final TourLogRepository tourLogRepository;

    private final TourRepository tourRepository;

    public List<TourLog> getAllLogsForTour(Long tourId) {
        try {
            return tourLogRepository.findByTourId(tourId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Could not find entities by tour id: " + tourId, e);
        }
    }

    public TourLog createLogForTour(Long tourId, TourLog tourLog) {
        if (tourLog.getId() != null) {
            throw new InvalidEntityException("New logs must not have an id.");
        }

        Tour tour = findTourById(tourId);

        try {
            tourLog.setTour(findTourById(tourId));
            tourLog.setDistanceInMeters(tour.getDistanceInMeters());
            tourLog.setDurationInSeconds(tour.getDurationInSeconds());

            return tourLogRepository.create(tourLog);
        } catch (Exception e) {
            throw new EntityCreateException("Could not create tour log: ", e);
        }
    }

    public TourLog updateLogForTour(Long tourId, TourLog tourLog) {
        if (!tourId.equals(tourLog.getId())) {
            throw new InvalidEntityException("Both tour and tour from tour log must have the same ID.");
        }

        TourLog existingTourLog = findTourLogById(tourLog.getId());

        try {
            TourLog updatedTourLog = TourLog.builder()
                    .id(existingTourLog.getId())
                    .tour(existingTourLog.getTour())
                    .date(tourLog.getDate())
                    .comment(tourLog.getComment())
                    .difficulty(tourLog.getDifficulty())
                    .rating(tourLog.getRating())
                    .distanceInMeters(existingTourLog.getDistanceInMeters())
                    .durationInSeconds(existingTourLog.getDurationInSeconds())
                    .build();

            return tourLogRepository.update(updatedTourLog);
        } catch (Exception e) {
            throw new EntityUpdateException("Could not update tour log with id: " + tourLog.getId(), e);
        }
    }

    public void deleteLogForTour(Long tourId, Long logId) {
        findTourById(tourId);

        try {
            tourLogRepository.delete(logId);
        } catch (Exception e) {
            throw new EntityDeleteException("Could not delete tour log with id: " + logId, e);
        }
    }

    private TourLog findTourLogById(Long logId) {
        return tourLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find tour log with id: " + logId));
    }

    private Tour findTourById(Long tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find tour with id: " + tourId));
    }
}