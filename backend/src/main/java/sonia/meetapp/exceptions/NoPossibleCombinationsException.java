package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class NoPossibleCombinationsException extends RuntimeException {

    public NoPossibleCombinationsException() {
        super("There is no way to connect people with those, who they have not already seen");
    }
}

