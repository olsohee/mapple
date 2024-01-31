package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class MeetingException extends BusinessException {
    public MeetingException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
