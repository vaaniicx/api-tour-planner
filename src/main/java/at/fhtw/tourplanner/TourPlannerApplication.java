package at.fhtw.tourplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "at.fhtw.tourplanner")
public class TourPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TourPlannerApplication.class, args);
    }
}
