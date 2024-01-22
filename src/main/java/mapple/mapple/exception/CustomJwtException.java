package mapple.mapple.exception;

public class CustomJwtException extends BusinessException{
    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
