package sonia.meetapp.app.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import sonia.meetapp.exceptions.PasswordNotMatchException;
import sonia.meetapp.exceptions.UserExistsException;
import sonia.meetapp.users.AppUser;
import sonia.meetapp.users.AppUserRepo;
import sonia.meetapp.users.NewAppUser;
import sonia.meetapp.users.UserService;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
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
                "qwertz", "insta");
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

    @Test
    void registerNewUserTestConstraintViolation() {

        NewAppUser newAppUser = new NewAppUser("abc@gmail.com", "qwer",
                "qwer", "insta");
        AppUser appUser = new AppUser("abc@gmail.com", "password_encode", "insta");

        when(passwordEncoder.encode(newAppUser.password())).thenReturn("password_encode");
        when(appUserRepo.findById(newAppUser.email())).thenReturn(Optional.empty());
        when(appUserRepo.save(appUser)).thenReturn(appUser);

        try {
            userService.registerNewUser(newAppUser);
            Assertions.fail("Expected exception was not thrown");
        } catch (ConstraintViolationException ignored) {
        }
    }

    @Test
    void loadUserByUsername() {
        String email = "userName@gmail.com";
        AppUser appUser = new AppUser("userName@gmail.com", "12345", "insta");
        User user = new User("userName@gmail.com", "12345", Collections.emptyList());
        when(appUserRepo.findById(email)).thenReturn(Optional.of(appUser));
        User actual = (User) userService.loadUserByUsername(email);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void loadUserByUsernameFail() {
        String email = "userName@gmail.com";
        when(appUserRepo.findById(email)).thenReturn(Optional.empty());
        assertNull(userService.loadUserByUsername(email));
    }
}


