package sonia.meetapp.events;

import lombok.Data;
import sonia.meetapp.app.participants.Participant;

import java.time.Instant;
import java.util.List;

@Data
public class Event {
    private String id;
    private String description;
    private String name;
    private String place;
    private Instant time;
    private List<Participant> eventParticipants;
    private List<List<Participant>> currentRound;
    private List<List<List<Participant>>> previousRounds;

    public Event(String id, String name, String place, Instant time, String description) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.time = time;
        this.description = description;
    }
}
