package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import sonia.meetapp.exceptions.PasswordNotMatchException;
import sonia.meetapp.exceptions.UserExistsException;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    AppUserRepo appUserRepo = mock(AppUserRepo.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    UserService userService = new UserService(appUserRepo, passwordEncoder);

    @Test
    void registerNewUserTest() {

        NewAppUser newAppUser = new NewAppUser("abc@gmail.com", "qwerty",
                "qwerty", "insta");
        AppUser appUser = new AppUser("abc@gmail.com", "password_encode", "insta");

        when(passwordEncoder.encode(newAppUser.password())).thenReturn("password_encode");
        when(appUserRepo.findById(newAppUser.email())).thenReturn(Optional.empty());
        when(appUserRepo.save(appUser)).thenReturn(appUser);


        AppUser actual = userService.registerNewUser(newAppUser);
        Assertions.assertEquals(appUser, actual);
    }

    @Test
    void registerNewUserTestEmailAlreadyExists() {

        NewAppUser newAppUser = new NewAppUser("abc@gmail.com", "qwerty",
                "qwerty", "insta");
        AppUser appUser = new AppUser("abc@gmail.com", "password_encode", "insta");

        when(passwordEncoder.encode(newAppUser.password())).thenReturn("password_encode");
        when(appUserRepo.findById(newAppUser.email())).thenReturn(Optional.of(appUser));
        when(appUserRepo.save(appUser)).thenReturn(appUser);

        try {
            userService.registerNewUser(newAppUser);
            Assertions.fail("Expected exception was not thrown");
        } catch (UserExistsException ignored) {
        }
    }

    @Test
    void registerNewUserTestPasswordDoesNotMatch() {

        NewAppUser newAppUser = new NewAppUser("abc@gmail.com", "qwerty",
                "qwert", "insta");
        AppUser appUser = new AppUser("abc@gmail.com", "password_encode", "insta");

        when(passwordEncoder.encode(newAppUser.password())).thenReturn("password_encode");
        when(appUserRepo.findById(newAppUser.email())).thenReturn(Optional.empty());
        when(appUserRepo.save(appUser)).thenReturn(appUser);

        try {
            userService.registerNewUser(newAppUser);
            Assertions.fail("Expected exception was not thrown");
        } catch (PasswordNotMatchException ignored) {
        }
    }
}


