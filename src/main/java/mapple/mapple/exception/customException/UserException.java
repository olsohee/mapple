package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class UserException extends BusinessException {
    public UserException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
