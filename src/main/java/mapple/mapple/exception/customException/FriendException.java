package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class FriendException extends BusinessException {
    public FriendException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
