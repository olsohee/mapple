package mapple.mapple.entity;

import lombok.AllArgsConstructor;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.ReviewException;

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
                .orElseThrow(() -> new ReviewException(ErrorCode.NOT_FOUND_RATING));
    }
}
