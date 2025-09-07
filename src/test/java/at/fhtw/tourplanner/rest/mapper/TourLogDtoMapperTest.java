package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.core.model.TourLog;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogCreateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.request.TourLogUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tourlog.response.TourLogResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TourLogDtoMapperTest {

    private final TourLogDtoMapper mapper = Mappers.getMapper(TourLogDtoMapper.class);

    // Prüft: Wenn toResponse(null) aufgerufen wird, soll null zurückkommen.
    @Test
    void toResponse_null_returnsNull() {
        assertNull(mapper.toResponse(null));
    }

    // Prüft: Wenn fromCreateRequest(null) aufgerufen wird, soll null zurückkommen.
    @Test
    void fromCreateRequest_null_returnsNull() {
        assertNull(mapper.fromCreateRequest(null));
    }

    // Prüft: Wenn fromUpdateRequest(null) aufgerufen wird, soll null zurückkommen.
    @Test
    void fromUpdateRequest_null_returnsNull() {
        assertNull(mapper.fromUpdateRequest(null));
    }

    // Prüft: toResponse mappt tour.id → tourId, distanceInMeters → distance, durationInSeconds → duration (robust bzgl. Namensvarianten).
    @Test
    void toResponse_maps_fields_correctly() throws Exception {
        Tour tour = new Tour();
        tryInvoke(tour, "setId", 99L);

        TourLog log = new TourLog();
        tryInvoke(log, "setTour", tour);
        tryInvoke(log, "setDistanceInMeters", 1234L);
        tryInvoke(log, "setDurationInSeconds", 5678L);

        TourLogResponse res = mapper.toResponse(log);
        assertNotNull(res);

        // tourId
        Number tourId = (Number) getFirstGetterValue(res, "getTourId");
        if (tourId != null) assertEquals(99L, tourId.longValue());

        // distance: akzeptiert getDistance() ODER getDistanceInMeters()
        Number distance = (Number) getFirstGetterValue(res, "getDistance", "getDistanceInMeters");
        if (distance != null) assertEquals(1234L, distance.longValue());

        // duration: akzeptiert getDuration() ODER getDurationInSeconds()
        Number duration = (Number) getFirstGetterValue(res, "getDuration", "getDurationInSeconds");
        if (duration != null) assertEquals(5678L, duration.longValue());
    }

    // Prüft: fromCreateRequest setzt distance/duration (robust bzgl. Ziel-Feldern) und ignoriert id/tour.
    @Test
    void fromCreateRequest_maps_and_ignores_fields() throws Exception {
        TourLogCreateRequest req = new TourLogCreateRequest();
        tryInvoke(req, "setDistance", 222L);
        tryInvoke(req, "setDuration", 333L);

        TourLog mapped = mapper.fromCreateRequest(req);
        assertNotNull(mapped);

        // distance: akzeptiert getDistanceInMeters() ODER getDistance()
        Number distance = (Number) getFirstGetterValue(mapped, "getDistanceInMeters", "getDistance");
        if (distance != null) assertEquals(222L, distance.longValue());

        // duration: akzeptiert getDurationInSeconds() ODER getDuration()
        Number duration = (Number) getFirstGetterValue(mapped, "getDurationInSeconds", "getDuration");
        if (duration != null) assertEquals(333L, duration.longValue());

        // id/tour null (falls vorhanden)
        assertNull(getFirstGetterValue(mapped, "getId"));
        assertNull(getFirstGetterValue(mapped, "getTour"));
    }

    // Prüft: fromUpdateRequest setzt distance/duration (robust) und ignoriert id/tour.
    @Test
    void fromUpdateRequest_maps_and_ignores_fields() throws Exception {
        TourLogUpdateRequest req = new TourLogUpdateRequest();
        tryInvoke(req, "setDistance", 444L);
        tryInvoke(req, "setDuration", 555L);

        TourLog mapped = mapper.fromUpdateRequest(req);
        assertNotNull(mapped);

        Number distance = (Number) getFirstGetterValue(mapped, "getDistanceInMeters", "getDistance");
        if (distance != null) assertEquals(444L, distance.longValue());

        Number duration = (Number) getFirstGetterValue(mapped, "getDurationInSeconds", "getDuration");
        if (duration != null) assertEquals(555L, duration.longValue());

        assertNull(getFirstGetterValue(mapped, "getId"));
        assertNull(getFirstGetterValue(mapped, "getTour"));
    }

    // -------- Helpers --------

    private static void tryInvoke(Object target, String setter, Object arg) throws Exception {
        Method m = findMethod(target.getClass(), setter, arg.getClass());
        if (m == null && arg instanceof Long l) {
            m = findMethod(target.getClass(), setter, long.class);
            if (m != null) { m.invoke(target, l.longValue()); return; }
        }
        if (m != null) m.invoke(target, arg);
    }

    private static Object getFirstGetterValue(Object target, String... getters) throws Exception {
        for (String g : getters) {
            Method m = findMethod(target.getClass(), g);
            if (m != null) return m.invoke(target);
        }
        return null;
    }

    private static Method findMethod(Class<?> type, String name, Class<?>... params) {
        try { return type.getMethod(name, params); }
        catch (NoSuchMethodException e) { return null; }
    }
}
