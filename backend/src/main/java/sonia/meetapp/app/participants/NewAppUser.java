package sonia.meetapp.app.participants;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record NewAppUser(@Id
                         @Email(message = "Email is not valid")
                         String email,
                         @Size(min = 6, message = "this password is to short")
                         String password,
                         @Size(min = 6, message = "this password is to short")
                         String repeatPassword,
                         @NotBlank(message = "mandatory field. It'd be unfair to receive contacts and not share yours, right?")
                         String contacts) {
}
