package mapple.mapple.user.entity;

import lombok.AllArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.OAuthException;

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
                .orElseThrow(() -> new OAuthException(ErrorCodeAndMessage.NOT_FOUND_OAUTH_PROVIDER));
    }
}
