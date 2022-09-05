package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserExistsException extends RuntimeException {
    public UserExistsException(String email) {
        super("User with email " + email + " already exists", null, false, false);
    }
}
