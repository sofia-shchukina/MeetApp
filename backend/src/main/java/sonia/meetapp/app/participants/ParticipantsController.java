package sonia.meetapp.app.participants;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/participants")
public class ParticipantsController {

    private final ParticipantsService participantsService;

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
        participantsService.deleteParticipant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Participant> editParticipant(
            @PathVariable String id,
            @RequestBody NewParticipant editedNewParticipant) {
        Participant updatedParticipant = participantsService.editParticipant(id, editedNewParticipant);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedParticipant);
    }

    @PutMapping("/likes/")
    public ResponseEntity<Participant> addLikes(
            @RequestBody
            Like like
    ) {
        Participant updatedParticipant = participantsService.addLikes(like);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedParticipant);
    }

    @GetMapping("likes/analysis/{id}")
    public List<Participant> receiveMatches(
            @PathVariable String id
    ) {
        return participantsService.receiveMatches(id);
    }

    @GetMapping("/pairs")
    public List<Participant> receivePairs() {
        return participantsService.receivePairs();
    }

}
