package at.fhtw.tourplanner.report.util;

public class DurationFormatter {

    private DurationFormatter() {}

    public static String format(double durationInSeconds) {
        int hours = (int) (durationInSeconds / 3600);
        int minutes = (int) (durationInSeconds % 3600) / 60;
        int seconds = (int) (durationInSeconds % 60);

        return hours > 0
                ? String.format("%02d:%02d:%02d (h:m:s)", hours, minutes, seconds)
                : String.format("%02d:%02d (m:s)", minutes, seconds);
    }
}
