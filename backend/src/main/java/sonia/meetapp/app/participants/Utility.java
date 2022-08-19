package sonia.meetapp.app.participants;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class Utility {

    public String createIdAsString() {
        return UUID.randomUUID().toString();
    }
}
