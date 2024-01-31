package mapple.mapple.exception.customException;

import mapple.mapple.exception.ErrorCodeAndMessage;

public class OAuthException extends BusinessException {
    public OAuthException(ErrorCodeAndMessage responseFormat) {
        super(responseFormat);
    }
}
