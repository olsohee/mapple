package mapple.mapple.exception;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.exception.customException.CustomJwtException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final MessageSource messageSource;

    @ExceptionHandler
    public ResponseEntity handleBusinessException(BusinessException e) {
        ErrorCodeAndMessage responseFormat = e.getResponseFormat();
        ErrorCode errorCode = responseFormat.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), responseFormat.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        String message = messageSource.getMessage(fieldError.getCode(), new Object[]{fieldError.getField()}, null, null);

        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.CODE_001.getCode(), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity handleJwtException(CustomJwtException e) {
        ErrorCodeAndMessage responseFormat = e.getResponseFormat();
        ErrorCode errorCode = responseFormat.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), responseFormat.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }
}
