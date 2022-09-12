package sonia.meetapp.events;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping()
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping()
    public ResponseEntity<Event> addEvent(
            @RequestBody NewEvent newEvent) {
        Event createdEvent = eventService.addEvent(newEvent);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdEvent);
    }
}
