package gimbalabs.unsigsbe.exception;


import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    public HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
