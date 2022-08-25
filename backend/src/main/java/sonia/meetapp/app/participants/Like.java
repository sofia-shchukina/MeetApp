package sonia.meetapp.app.participants;

import lombok.Data;

@Data
public class Like {
    private String likerID;
    private String[] likedPeopleIDs;
}
