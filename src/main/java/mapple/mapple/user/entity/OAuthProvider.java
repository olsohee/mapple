package mapple.mapple.user.entity;

import lombok.AllArgsConstructor;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.OAuthException;

import java.util.Arrays;

@AllArgsConstructor
public enum OAuthProvider {

    NONE(""),
    KAKAO("kakao"),
    NAVER("naver");

    private String name;

    public static OAuthProvider find(String registrationId) {
        return Arrays.stream(OAuthProvider.values())
                .filter(oAuthProvider -> oAuthProvider.name.equals(registrationId))
                .findAny()
                .orElseThrow(() -> new OAuthException(ErrorCode.NOT_FOUND_PROVIDER));
    }
}
