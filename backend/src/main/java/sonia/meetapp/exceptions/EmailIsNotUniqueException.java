package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class EmailIsNotUniqueException extends RuntimeException {
    public EmailIsNotUniqueException() {
        super("User with this email is already registered for this event");
    }
}
