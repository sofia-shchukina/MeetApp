package sonia.meetapp.app.participants;

import com.google.common.collect.Lists;
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

    @GetMapping("/{eventId}")
    public List<Participant> getAllParticipants(@PathVariable String eventId) {
        return participantsService.getAllParticipants(eventId);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Participant> addParticipant(@PathVariable String eventId,
                                                      @RequestBody NewParticipant newParticipant) {
        Participant createdParticipant = participantsService.addParticipant(newParticipant, eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdParticipant);
    }

    @DeleteMapping("/{eventId}/{participantId}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable String eventId, @PathVariable String participantId) {
        participantsService.deleteParticipant(eventId, participantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit/{eventId}/{participantId}")
    public ResponseEntity<Participant> editParticipant(
            @PathVariable String eventId, @PathVariable String participantId,
            @RequestBody NewParticipant editedNewParticipant) {
        Participant updatedParticipant = participantsService.editParticipant(participantId, eventId, editedNewParticipant);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedParticipant);
    }

    @PutMapping("/likes/{eventId}")
    public ResponseEntity<Participant> addLikes(@PathVariable String eventId,
                                                @RequestBody
                                                Like like
    ) {
        Participant updatedParticipant = participantsService.addLikes(like, eventId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedParticipant);
    }

    @GetMapping("likes/analysis/{eventId}/{participantId}")
    public List<Participant> receiveMatches(
            @PathVariable String eventId,
            @PathVariable String participantId
    ) {
        return participantsService.receiveMatches(eventId, participantId);
    }

    @GetMapping("/pairs")
    public List<List<Participant>> receivePairs() {
        List<Participant> pairs = participantsService.receivePairs();
        return Lists.partition(pairs, 2);
    }

}
