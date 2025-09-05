package at.fhtw.tourplanner.rest.dto.tour.response;

import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TourExportResponse {

    private TourResponse tour;

    private List<TourLogResponse> logs;
}
