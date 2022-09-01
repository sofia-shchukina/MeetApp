package sonia.meetapp.app.participants;

import org.springframework.data.annotation.Id;

public record AppUser(@Id
                      String email,
                      String passwordHash,
                      String contacts) {

}
