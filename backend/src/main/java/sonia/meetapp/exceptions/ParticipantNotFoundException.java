package sonia.meetapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException(String participantId) {
        super("Participant with ID " + participantId + " does not exist", null, false, false);
    }
}
