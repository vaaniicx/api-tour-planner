package at.fhtw.tourplanner.core.service;

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
        log.info("Get all logs for tour with id={}", tourId);

        return tourLogRepository.findByTourId(tourId);
    }

    public TourLog createLogForTour(Long tourId, TourLog tourLog) throws NoSuchElementException {
        log.info("Create new log for tour with id={}", tourId);

        if (tourLog.getId() != null) {
            throw new IllegalArgumentException("New logs must not have an ID.");
        }

        Tour tour = findTourById(tourId);

        tourLog.setTour(findTourById(tourId));
        tourLog.setDistanceInMeters(tour.getDistanceInMeters());
        tourLog.setDurationInSeconds(tour.getDurationInSeconds());

        return tourLogRepository.create(tourLog);
    }

    public TourLog updateLogForTour(Long tourId, TourLog tourLog) throws NoSuchElementException {
        log.info("Update log for tour with id={}", tourId);

        TourLog existingTourLog = findTourLogById(tourLog.getId());

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
    }

    public void deleteLogForTour(Long tourId, Long logId) {
        log.info("Delete log with id={}", logId);

        findTourById(tourId);
        tourLogRepository.delete(logId);
    }

    private TourLog findTourLogById(Long logId) {
        return tourLogRepository.findById(logId)
                .orElseThrow(() -> new NoSuchElementException("Log not found."));
    }

    private Tour findTourById(Long tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new NoSuchElementException("Tour not found."));
    }
}