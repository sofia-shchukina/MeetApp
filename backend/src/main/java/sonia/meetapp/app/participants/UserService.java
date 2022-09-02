package sonia.meetapp.app.participants;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.PasswordNotMatchException;
import sonia.meetapp.exceptions.UserExistsException;

import javax.validation.*;
import java.util.Set;


@Service
public class UserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private Validator validator;

    public UserService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }


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
        validator = factory.getValidator();
        Set<ConstraintViolation<NewAppUser>> violations = validator.validate(newAppUser);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
