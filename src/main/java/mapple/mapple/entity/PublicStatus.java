package mapple.mapple.entity;

import lombok.AllArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.ReviewException;

import java.util.Arrays;

@AllArgsConstructor
public enum PublicStatus {

    PUBLIC("공개"),
    ONLY_FRIEND("친구공개"),
    PRIVATE("비공개");

    private final String name;

    public static PublicStatus find(String name) {
        return Arrays.stream(PublicStatus.values())
                .filter(status -> status.name.equals(name))
                .findAny()
                .orElseThrow(() -> new ReviewException(ErrorCodeAndMessage.INVALID_PUBLIC_STATUS));
    }
}
