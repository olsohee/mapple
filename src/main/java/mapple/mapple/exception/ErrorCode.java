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

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "user.duplicated.email"),
    NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST, "user.not_found.email"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "user.invalid.password"),
    NOT_FOUND_PUBLIC_STATUS(HttpStatus.BAD_REQUEST, "review.not_found.public_status"),
    NOT_FOUND_RATING(HttpStatus.BAD_REQUEST, "review.not_found.rating"),

    // jwt
    NOT_INPUT_TOKEN(HttpStatus.BAD_REQUEST, "jwt.not_input"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "jwt.expired"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "jwt.invalid"),

    // oAuth
    NOT_FOUND_PROVIDER(HttpStatus.BAD_REQUEST, "oauth.not_found.provider")

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

