package sonia.meetapp.users;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.PasswordNotMatchException;
import sonia.meetapp.exceptions.UserExistsException;

import javax.validation.*;
import java.util.Collections;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;

    public AppUser registerNewUser(NewAppUser newAppUser) {
        validateUser(newAppUser);
        if (appUserRepo.findById(newAppUser.email()).isPresent()) {
            throw new UserExistsException(newAppUser.email());
        } else if (!newAppUser.password().equals(newAppUser.repeatPassword())) {
            throw new PasswordNotMatchException();
        }
        AppUser appUser = new AppUser(newAppUser.email(), passwordEncoder.encode(newAppUser.password()), newAppUser.contacts());
        return appUserRepo.save(appUser);
    }

    void validateUser(NewAppUser newAppUser) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NewAppUser>> violations = validator.validate(newAppUser);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findById(email)
                .orElse(null);
        if (appUser == null) {
            return null;
        }
        return new User(appUser.email(), appUser.passwordHash(), Collections.emptyList());
    }
}
