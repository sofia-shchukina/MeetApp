package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    public Map<String, Object> hashmapBuilder(String string) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("message", string);
        return responseBody;
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleParticipantNotFoundException(ParticipantNotFoundException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameIsNotUniqueException.class)
    public ResponseEntity<Map<String, Object>> handleNameIsNotUniqueException(NameIsNotUniqueException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailIsNotUniqueException.class)
    public ResponseEntity<Map<String, Object>> handleEmailIsNotUniqueException(EmailIsNotUniqueException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExistsException(UserExistsException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordNotMatchException(PasswordNotMatchException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAppUserNotFoundException(AppUserNotFoundException exception) {
        Map<String, Object> responseBody = hashmapBuilder(exception.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
}
