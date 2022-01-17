package gimbalabs.unsigsbe.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class DatumMismatchException extends ApiException{
    public DatumMismatchException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
