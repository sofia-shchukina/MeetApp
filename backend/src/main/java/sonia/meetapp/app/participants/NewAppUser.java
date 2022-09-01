package sonia.meetapp.app.participants;

import org.springframework.data.annotation.Id;

public record NewAppUser(@Id
                         String email,
                         String password,
                         String repeatPassword,
                         String contacts) {
}
