package at.fhtw.tourplanner.core.model;

import lombok.Getter;

@Getter
public enum Rating {
    PERFECT(3),
    GOOD(2),
    BAD(1),
    NONE(0);

    private final int score;

    Rating(int score) {
        this.score = score;
    }
}
