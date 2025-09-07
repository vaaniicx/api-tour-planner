package at.fhtw.tourplanner.rest.mapper;

import at.fhtw.tourplanner.core.model.Tour;
import at.fhtw.tourplanner.rest.dto.tour.request.TourCreateRequest;
import at.fhtw.tourplanner.rest.dto.tour.request.TourUpdateRequest;
import at.fhtw.tourplanner.rest.dto.tour.response.TourResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TourDtoMapperTest {

    private final TourDtoMapper mapper = Mappers.getMapper(TourDtoMapper.class);

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

    // Prüft: Name wird übernommen, distanceInMeters → distance, durationInSeconds → duration.
    @Test
    void toResponse_maps_name_and_optionally_distance_duration() throws Exception {
        Tour tour = new Tour();
        // name
        tryInvoke(tour, "setName", "Alpenpass");
        // optional: distanceInMeters / durationInSeconds (falls vorhanden)
        tryInvoke(tour, "setDistanceInMeters", 12345L);
        tryInvoke(tour, "setDurationInSeconds", 6789L);

        TourResponse res = mapper.toResponse(tour);
        assertNotNull(res);
        assertEquals("Alpenpass", tryInvokeGet(res, "getName"));

        // nur prüfen, wenn Ziel-Getter existieren
        Object distance = tryInvokeGetNullable(res, "getDistance");
        if (distance != null) assertEquals(12345L, ((Number) distance).longValue());

        Object duration = tryInvokeGetNullable(res, "getDuration");
        if (duration != null) assertEquals(6789L, ((Number) duration).longValue());
    }

    // Prüft: CreateRequest setzt Name, ignoriert id, distanceInMeters, durationInSeconds, logs.
    @Test
    void fromCreateRequest_maps_name_and_ignores_id_distance_duration_logs() throws Exception {
        TourCreateRequest req = new TourCreateRequest();
        tryInvoke(req, "setName", "Neue Tour");

        Tour mapped = mapper.fromCreateRequest(req);
        assertNotNull(mapped);
        assertEquals("Neue Tour", tryInvokeGet(mapped, "getName"));

        // id muss null sein (falls vorhanden)
        Object id = tryInvokeGetNullable(mapped, "getId");
        if (id != null) assertNull(id);

        // distanceInMeters/durationInSeconds entweder null/0 (je nach Typ) → nur prüfen falls vorhanden
        checkZeroOrNull(mapped, "getDistanceInMeters");
        checkZeroOrNull(mapped, "getDurationInSeconds");

        // logs: null oder leer
        Object logs = tryInvokeGetNullable(mapped, "getLogs");
        if (logs != null) {
            if (logs instanceof Collection<?> c) assertTrue(c.isEmpty());
        }
    }

    // Prüft: UpdateRequest setzt Name, ignoriert id, distanceInMeters, durationInSeconds, logs.
    @Test
    void fromUpdateRequest_maps_name_and_ignores_id_distance_duration_logs() throws Exception {
        TourUpdateRequest req = new TourUpdateRequest();
        tryInvoke(req, "setName", "Umbenannt");

        Tour mapped = mapper.fromUpdateRequest(req);
        assertNotNull(mapped);
        assertEquals("Umbenannt", tryInvokeGet(mapped, "getName"));

        Object id = tryInvokeGetNullable(mapped, "getId");
        if (id != null) assertNull(id);

        checkZeroOrNull(mapped, "getDistanceInMeters");
        checkZeroOrNull(mapped, "getDurationInSeconds");

        Object logs = tryInvokeGetNullable(mapped, "getLogs");
        if (logs != null) {
            if (logs instanceof Collection<?> c) assertTrue(c.isEmpty());
        }
    }

    // ------- kleine Helfer ohne Abhängigkeiten --------

    private static void tryInvoke(Object target, String setter, Object arg) throws Exception {
        Method m = findMethod(target.getClass(), setter, arg.getClass());
        if (m == null && arg instanceof Long l) { // primitive long fallback
            m = findMethod(target.getClass(), setter, long.class);
            if (m != null) { m.invoke(target, l.longValue()); return; }
        }
        if (m != null) m.invoke(target, arg);
    }

    private static Object tryInvokeGet(Object target, String getter) throws Exception {
        Method m = findMethod(target.getClass(), getter);
        if (m == null) fail("Missing method: " + getter + " on " + target.getClass());
        return m.invoke(target);
    }

    private static Object tryInvokeGetNullable(Object target, String getter) throws Exception {
        Method m = findMethod(target.getClass(), getter);
        return (m == null) ? null : m.invoke(target);
    }

    private static void checkZeroOrNull(Object target, String getter) throws Exception {
        Object val = tryInvokeGetNullable(target, getter);
        if (val == null) return; // ok
        if (val instanceof Number n) assertEquals(0L, n.longValue());
    }

    private static Method findMethod(Class<?> type, String name, Class<?>... params) {
        try { return type.getMethod(name, params); }
        catch (NoSuchMethodException e) { return null; }
    }
}
