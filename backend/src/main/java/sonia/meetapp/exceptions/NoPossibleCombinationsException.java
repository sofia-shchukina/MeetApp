package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class NoPossibleCombinationsException extends RuntimeException {

    public NoPossibleCombinationsException() {
        super("That's all, folks!");
    }
}

