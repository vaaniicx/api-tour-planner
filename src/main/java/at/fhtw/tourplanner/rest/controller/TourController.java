package at.fhtw.tourplanner.rest.controller;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.service.TourService;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import at.fhtw.tourplanner.rest.mapper.TourDtoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    private final TourDtoMapper tourDtoMapper;

    private final ObjectMapper objectMapper;

    @GetMapping
    public List<TourResponse> getAllTours() {
        return tourService.getAllTours()
                .stream()
                .map(tourDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exportTours() throws JsonProcessingException {
        List<TourResponse> allTours = getAllTours();
        String response = objectMapper.writeValueAsString(allTours);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"all-tours.json\"")
                .body(response);
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<TourResponse> getTourById(@PathVariable("tourId") Long tourId) {
        try {
            Tour tourById = tourService.getTourById(tourId);
            return ResponseEntity.ok(tourDtoMapper.toResponse(tourById));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/export/{tourId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> exportTourById(@PathVariable("tourId") Long tourId) throws JsonProcessingException {
        ResponseEntity<TourResponse> tourById = getTourById(tourId);
        String response = objectMapper.writeValueAsString(tourById);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tour.json\"")
                .body(response);
    }

    @PostMapping
    public ResponseEntity<TourResponse> createTour(@RequestBody TourCreateRequest request) {
        try {
            Tour createdTour = tourService.createTour(tourDtoMapper.fromCreateRequest(request));
            return ResponseEntity.ok(tourDtoMapper.toResponse(createdTour));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{tourId}")
    public ResponseEntity<TourResponse> updateTour(@PathVariable("tourId") Long tourId,
                                                   @RequestBody TourUpdateRequest request) {
        try {
            Tour tourToBeUpdated = tourDtoMapper.fromUpdateRequest(request);
            Tour updatedTour = tourService.updateTour(tourId, tourToBeUpdated);
            return ResponseEntity.ok(tourDtoMapper.toResponse(updatedTour));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> deleteTour(@PathVariable("tourId") Long tourId) {
        try {
            tourService.deleteTour(tourId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
