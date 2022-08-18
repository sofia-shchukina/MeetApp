package sonia.meetapp.app.participants;

import lombok.Data;

import java.util.UUID;

@Data
public class NewParticipant {
    String name;

    public Participant withRandomID(NewParticipant newparticipant) {
        return new Participant(newparticipant.name, UUID.randomUUID().toString());
    }

}
