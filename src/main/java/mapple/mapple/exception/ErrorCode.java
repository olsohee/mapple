package mapple.mapple.exception;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),

    // user
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "user.duplicated.email"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "user.not_found.user"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "user.invalid.password"),


    // jwt
    NOT_INPUT_TOKEN(HttpStatus.BAD_REQUEST, "jwt.not_input"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "jwt.expired"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "jwt.invalid"),
    NOT_FOUND_TOKEN_IN_REDIS(HttpStatus.BAD_REQUEST, "jwt.not_found"),

    // oAuth
    NOT_FOUND_PROVIDER(HttpStatus.BAD_REQUEST, "oauth.not_found.provider"),

    // review
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "review.not_found"),
    NOT_FOUND_PUBLIC_STATUS(HttpStatus.BAD_REQUEST, "review.not_found.public_status"),
    NOT_FOUND_RATING(HttpStatus.BAD_REQUEST, "review.not_found.rating"),

    // friend
    CAN_NOT_FRIEND_SAME_USER(HttpStatus.BAD_REQUEST, "friend.can_not_same_user"),
    NOT_FOUND_FRIEND(HttpStatus.BAD_REQUEST, "friend.not_found"),

    // meeting
    NOT_FOUND_MEETING(HttpStatus.BAD_REQUEST, "meeting.not_found"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private String message;

    @Component
    @RequiredArgsConstructor
    public static class MessageInjector {

        private final MessageSource messageSource;

        @PostConstruct
        public void injectMessage() {
            Arrays.stream(ErrorCode.values())
                    .forEach(errorCode -> errorCode.message =
                            messageSource.getMessage(errorCode.errorCode, null, null));
        }
    }
}

