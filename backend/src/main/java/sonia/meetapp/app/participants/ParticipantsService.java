package sonia.meetapp.app.participants;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantsService {
    private final ParticipantsRepo participantsRepo;

    public ParticipantsService(ParticipantsRepo participantsRepo) {

        this.participantsRepo = participantsRepo;
    }

    public List<Participant> getAllParticipants() {

        return participantsRepo.findAll();
    }

    public Participant addParticipant(String nameOfParticipant) {
        return participantsRepo.save(new Participant(nameOfParticipant, UUID.randomUUID().toString()));
    }
}
