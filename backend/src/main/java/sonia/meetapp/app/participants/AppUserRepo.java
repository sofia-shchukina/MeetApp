package sonia.meetapp.app.participants;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepo extends MongoRepository<AppUser, String> {
}
