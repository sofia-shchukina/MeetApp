package sonia.meetapp.app.participants;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class ParticipantsService {
    private final ParticipantsRepo participantsRepo;
    private final Utility utility;

    public ParticipantsService(ParticipantsRepo participantsRepo, Utility utility) {
        this.participantsRepo = participantsRepo;
        this.utility = utility;
    }

    public List<Participant> getAllParticipants() {
        return participantsRepo.findAll();
    }

    public Participant addParticipant(NewParticipant newParticipant) {
        Participant participant = new Participant(newParticipant.getName(), utility.createIdAsString());
        return participantsRepo.save(participant);
    }

    public void deleteParticipant(String id) {
        if (participantsRepo.existsById(id)) {
            participantsRepo.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no participant with this id");
        }
    }

    public Participant editParticipant(String id, NewParticipant editedNewParticipant) {
        if (participantsRepo.existsById(id)) {
            participantsRepo.deleteById(id);
            return participantsRepo.save(new Participant(editedNewParticipant.getName(), id));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no participant with this id");
        }
    }
}
