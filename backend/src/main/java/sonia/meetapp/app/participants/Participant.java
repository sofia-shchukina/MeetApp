package sonia.meetapp.app.participants;

import lombok.Data;

@Data
public class Participant {
    String name;
    String id;

    public Participant(String name, String id) {
        this.name = name;
        this.id = id;
    }


}