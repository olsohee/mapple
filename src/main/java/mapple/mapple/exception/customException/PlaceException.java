package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class PlaceException extends BusinessException {
    public PlaceException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
