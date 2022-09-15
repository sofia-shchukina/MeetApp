package sonia.meetapp.events;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sonia.meetapp.app.participants.Utility;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepo eventRepo;
    private final Utility utility;

    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    public Event addEvent(NewEvent newEvent) {
        Event event = new Event(utility.createIdAsString(), newEvent.getName(), newEvent.getPlace(), newEvent.getTime(), newEvent.getDescription());
        event.setEventParticipants(new ArrayList<>());
        return eventRepo.save(event);
    }
}
