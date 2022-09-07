package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(String email) {
        super("Participant with email " + email + " does not exist", null, false, false);
    }
}
