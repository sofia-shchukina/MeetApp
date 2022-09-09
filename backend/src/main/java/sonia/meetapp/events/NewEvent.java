package sonia.meetapp.events;

import lombok.Data;

@Data
public class NewEvent {

    private String description;
    private String name;
    private String place;
    private String time;
}
