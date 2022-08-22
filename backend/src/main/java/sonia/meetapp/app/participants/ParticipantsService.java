package sonia.meetapp.app.participants;


import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.NameIsNotUniqueException;
import sonia.meetapp.exceptions.ParticipantNotFoundException;

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
        if (Boolean.TRUE.equals(thisNameIsUnique(newParticipant))) {
            return participantsRepo.save(participant);
        } else {
            throw new NameIsNotUniqueException();
        }
    }

    public void deleteParticipant(String id) {
        if (participantsRepo.existsById(id)) {
            participantsRepo.deleteById(id);
        } else {
            throw new ParticipantNotFoundException(id);
        }
    }


    public Participant editParticipant(String id, NewParticipant editedNewParticipant) {
        if (Boolean.TRUE.equals(thisNameIsUnique(editedNewParticipant))) {
            if (participantsRepo.existsById(id)) {
                return participantsRepo.save(new Participant(editedNewParticipant.getName(), id));
            } else {
                throw new ParticipantNotFoundException(id);
            }
        } else {
            throw new NameIsNotUniqueException();
        }
    }

    public Boolean thisNameIsUnique(NewParticipant newParticipant) {
        String name = newParticipant.getName();
        List<Participant> allParticipants = participantsRepo.findAll();

        for (Participant allParticipant : allParticipants) {
            if (allParticipant.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
