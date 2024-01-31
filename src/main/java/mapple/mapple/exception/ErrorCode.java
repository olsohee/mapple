package mapple.mapple.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CODE_001("001", HttpStatus.BAD_REQUEST),
    CODE_002("002", HttpStatus.BAD_REQUEST),
    CODE_003("003", HttpStatus.INTERNAL_SERVER_ERROR),
    CODE_004("004", HttpStatus.UNAUTHORIZED),
    CODE_005("005", HttpStatus.FORBIDDEN),
    CODE_006("006", HttpStatus.BAD_REQUEST),
    CODE_007("007", HttpStatus.BAD_REQUEST),
    CODE_008("008", HttpStatus.BAD_REQUEST),
    CODE_009("009", HttpStatus.BAD_REQUEST);

    private final String code;
    private final HttpStatus httpStatus;
}
