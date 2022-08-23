package sonia.meetapp.app.participants;

import lombok.Data;

import java.util.List;

@Data
public class Participant {
    private String name;
    private String id;

    private List<String> peopleILike;
    private List<String> peopleWhoLikeMe;

    public Participant(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
