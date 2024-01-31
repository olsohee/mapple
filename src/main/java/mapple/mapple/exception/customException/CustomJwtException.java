package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class CustomJwtException extends BusinessException {
    public CustomJwtException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
