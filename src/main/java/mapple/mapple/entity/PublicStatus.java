package mapple.mapple.entity;

import lombok.AllArgsConstructor;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.ReviewException;

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
                .orElseThrow(() -> new ReviewException(ErrorCode.NOT_FOUND_PUBLIC_STATUS));
    }
}
