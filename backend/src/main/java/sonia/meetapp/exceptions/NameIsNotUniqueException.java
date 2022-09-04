package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NameIsNotUniqueException extends RuntimeException {

    public NameIsNotUniqueException() {
        super("Unfortunately somebody already uses this name, please choose another one. Hint: add the first letter of your surname");
    }
}
