package sonia.meetapp.users;

import org.springframework.data.annotation.Id;

public record AppUser(@Id
                      String email,
                      String passwordHash,
                      String contacts,
                      String role) {
}
