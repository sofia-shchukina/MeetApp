package sonia.meetapp.app.participants;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantsRepo extends MongoRepository<Participant, String> {
}
