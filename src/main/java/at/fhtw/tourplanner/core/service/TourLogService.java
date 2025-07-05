package at.fhtw.tourplanner.core.service;

import at.fhtw.tourplanner.core.model.TourLog;

import java.util.List;

public interface TourLogService {

    List<TourLog> getAllLogsForTour(Long tourId);

    TourLog createLogForTour(Long tourId, TourLog tourLog);

    TourLog updateLogForTour(Long tourId, TourLog tourLog);

    void deleteLogForTour(Long tourId, Long logId);
}
