package gimbalabs.unsigsbe.exception;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


@ControllerAdvice
public class RestApiErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(RestApiErrorHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(UnifiedMap.newWithKeysValues("message", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(UnifiedMap.newWithKeysValues("message", ex.getMessage()), ex.httpStatus);
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleApiException(ResponseStatusException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(UnifiedMap.newWithKeysValues("message", ex.getMessage()), ex.getStatus());
    }

}
