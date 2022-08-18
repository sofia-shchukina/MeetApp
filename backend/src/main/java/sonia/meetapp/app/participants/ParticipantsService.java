package sonia.meetapp.app.participants;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ParticipantsService {
    private final ParticipantsRepo participantsRepo;

    public ParticipantsService(ParticipantsRepo participantsRepo) {

        this.participantsRepo = participantsRepo;
    }

    public List<Participant> getAllParticipants() {

        return participantsRepo.findAll();
    }

    public Participant addParticipant(NewParticipant newParticipant) {
        return participantsRepo.save(newParticipant.withRandomID(newParticipant));
    }
}
