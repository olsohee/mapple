package mapple.mapple.exception.customException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;

@RequiredArgsConstructor
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCodeAndMessage responseFormat;
}
