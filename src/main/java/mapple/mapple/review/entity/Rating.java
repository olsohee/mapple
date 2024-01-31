package mapple.mapple.review.entity;

import lombok.AllArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.ReviewException;

import java.util.Arrays;

@AllArgsConstructor
public enum Rating {

    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5")
    ;

    private final String name;

    public static Rating find(String name) {
        return Arrays.stream(Rating.values())
                .filter(rating -> rating.name.equals(name))
                .findAny()
                .orElseThrow(() -> new ReviewException(ErrorCodeAndMessage.INVALID_RATING));
    }
}
