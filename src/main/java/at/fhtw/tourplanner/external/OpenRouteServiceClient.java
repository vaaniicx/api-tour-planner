package at.fhtw.tourplanner.external;

import at.fhtw.tourplanner.core.model.Location;
import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TransportType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class OpenRouteServiceClient {

    @Value("${openrouteservice.api.baseurl}")
    private static String BASE_URL;

    @Value("${openrouteservice.api.key}")
    private static String API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    public RouteInformation getRouteInformation(Tour tour) {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(BASE_URL + getProfile(tour.getTransportType())))
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addEncodedQueryParameter("start", buildLocationQueryParam(tour.getFrom()))
                .addEncodedQueryParameter("end", buildLocationQueryParam(tour.getTo()))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode json = mapper.readTree(response.body().string());
            JsonNode summary = json.get("features").get(0).get("properties").get("summary");

            double distance = summary.get("distance").asDouble();
            double duration = summary.get("duration").asDouble();
            return new RouteInformation(distance, duration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getProfile(TransportType transportType) {
        return switch (transportType) {
            case CAR -> "driving-car";
            case BIKE -> "cycling-regular";
            case WALK -> "foot-walking";
            case HIKE -> "foot-hiking";
            case WHEELCHAIR -> "wheelchair";
        };
    }

    private String buildLocationQueryParam(Location location) {
        String longitude = location.getLongitude();
        String latitude = location.getLatitude();
        return longitude + "," + latitude;
    }
}
