package mapple.mapple.exception;

public class OAuthException extends BusinessException {
    public OAuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
