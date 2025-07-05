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
public class TourLogServiceImpl implements TourLogService {

    private final TourLogRepository tourLogRepository;

    private final TourRepository tourRepository;

    @Override
    public List<TourLog> getAllLogsForTour(Long tourId) {
        log.info("Get all logs for tour with id={}", tourId);

        return tourLogRepository.findByTourId(tourId);
    }

    @Override
    public TourLog createLogForTour(Long tourId, TourLog tourLog) throws NoSuchElementException {
        log.info("Create new log for tour with id={}", tourId);

        if (tourLog.getId() != null) {
            throw new IllegalArgumentException("New logs must not have an ID.");
        }

        tourLog.setTour(findTourById(tourId));
        return tourLogRepository.create(tourLog);
    }

    @Override
    public TourLog updateLogForTour(Long tourId, TourLog tourLog) throws NoSuchElementException {
        log.info("Update log for tour with id={}", tourId);

        TourLog existingTourLog = findTourLogById(tourLog.getId());
        TourLog updatedTourLog = new TourLog(existingTourLog.getId(), existingTourLog.getTour(), tourLog.getDate(), tourLog.getComment());
        return tourLogRepository.update(updatedTourLog);
    }

    @Override
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