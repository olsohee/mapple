package mapple.mapple.exception;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static mapple.mapple.exception.ErrorCode.*;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeAndMessage {

    /**
     * common
     */
    FORBIDDEN(CODE_005, "forbidden"),

    /**
     * user
     */
    NOT_FOUND_USER(CODE_001, "user.not_found.user"),
    DUPLICATED_EMAIL(CODE_001, "user.duplicated.email"),
    INVALID_PASSWORD(CODE_001, "user.invalid.password"),

    /**
     * review
     */
    NOT_FOUND_REVIEW(CODE_001, "review.not_found"),
    INVALID_PUBLIC_STATUS(CODE_001, "review.invalid.public_status"),
    INVALID_RATING(CODE_001, "review.invalid.rating"),

    /**
     * friend
     */
    NOT_FOUND_FRIEND(CODE_001, "friend.not_found"),
    CAN_NOT_FRIEND_SAME_USER(CODE_001, "friend.can_not_same_user"),

    /**
     * meeting
     */
    NOT_FOUND_MEETING(CODE_001, "meeting.not_found"),

    /**
     * place
     */
    NOT_FOUND_PLACE(CODE_001, "place.not_found"),

    /**
     * token
     */
    REQUIRED_TOKEN(CODE_004, "jwt.required"),
    EXPIRED_TOKEN(CODE_004, "jwt.expired"),
    INVALID_TOKEN(CODE_004, "jwt.invalid"),

    /**
     * oAuth
     */
    NOT_FOUND_OAUTH_PROVIDER(CODE_009, "oauth.not_found.provider"),
    ;

    private final ErrorCode errorCode;
    private final String messageCode;
    private String message;


    @Component
    @RequiredArgsConstructor
    public static class MessageInjector {

        private final MessageSource messageSource;

        @PostConstruct
        public void injectMessage() {
            Arrays.stream(ErrorCodeAndMessage.values())
                    .forEach(errorResponseFormat -> errorResponseFormat.message =
                            messageSource.getMessage(errorResponseFormat.messageCode, null, null));
        }
    }
}
