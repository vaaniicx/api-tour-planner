package at.fhtw.tourplanner.report.util;

public class DistanceConverter {

    private DistanceConverter() {}

    public static double metersToKilometers(double meters) {
        return (double) Math.round((meters / 1000) * 100) / 100;
    }
}
