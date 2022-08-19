package sonia.meetapp.app.participants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantsController {

    private final ParticipantsService participantsService;

    public ParticipantsController(ParticipantsService participantsService) {
        this.participantsService = participantsService;
    }

    @GetMapping()
    public List<Participant> getAllParticipants() {

        return participantsService.getAllParticipants();
    }

    @PostMapping()
    public ResponseEntity<Participant> addParticipant(
            @RequestBody NewParticipant newParticipant) {

        Participant createdParticipant = participantsService.addParticipant(newParticipant);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdParticipant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable String id) {

        if (participantsService.deleteParticipant(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
