package sonia.meetapp.app.participants;

import lombok.Data;

@Data
public class Participant {
    private String name;
    private String id;

    public Participant(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
