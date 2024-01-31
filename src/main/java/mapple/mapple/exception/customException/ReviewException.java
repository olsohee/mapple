package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class ReviewException extends BusinessException {
    public ReviewException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
