package sonia.meetapp.events;

import lombok.Data;

import java.time.Instant;

@Data
public class NewEvent {
    private String description;
    private String name;
    private String place;
    private Instant time;
}
