package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("Passwords don't match");
    }
}
