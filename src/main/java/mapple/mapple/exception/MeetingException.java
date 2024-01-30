package mapple.mapple.exception;

public class MeetingException extends BusinessException {
    public MeetingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
