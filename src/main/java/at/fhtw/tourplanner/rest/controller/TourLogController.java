package at.fhtw.tourplanner.rest.controller;

import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.core.service.TourLogService;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import at.fhtw.tourplanner.rest.mapper.TourLogDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tours/{tourId}/logs")
public class TourLogController {

    private final TourLogService tourLogService;

    private final TourLogDtoMapper tourLogDtoMapper;

    @GetMapping
    public List<TourLogResponse> getAllLogsForTour(@PathVariable("tourId") Long id) {
        return tourLogService.getAllLogsForTour(id)
                .stream().map(tourLogDtoMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TourLogResponse> createLogForTour(@PathVariable("tourId") Long tourId,
                                                            @RequestBody TourLogCreateRequest request) {
        TourLog tourLogToBeCreated = tourLogDtoMapper.fromCreateRequest(request);
        TourLog createdTourLog = tourLogService.createLogForTour(tourId, tourLogToBeCreated);
        return ResponseEntity
                .created(URI.create("/api/tours/" + tourId + "/logs/" + createdTourLog.getId()))
                .body(tourLogDtoMapper.toResponse(createdTourLog));
    }

    @PutMapping("/{logId}")
    public ResponseEntity<TourLogResponse> updateLogForTour(@PathVariable("tourId") Long tourId,
                                                            @PathVariable("logId") Long logId,
                                                            @RequestBody TourLogUpdateRequest request) {
        TourLog tourLogToBeUpdated = tourLogDtoMapper.fromUpdateRequest(request);
        tourLogToBeUpdated.setId(logId);

        TourLog updatedTourLog = tourLogService.updateLogForTour(tourId, tourLogToBeUpdated);
        return ResponseEntity.ok(tourLogDtoMapper.toResponse(updatedTourLog));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLogForTour(@PathVariable("tourId") Long tourId,
                                                 @PathVariable("logId") Long logId) {
        tourLogService.deleteLogForTour(tourId, logId);
        return ResponseEntity.noContent().build();
    }
}
