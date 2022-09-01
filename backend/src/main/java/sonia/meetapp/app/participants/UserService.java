package sonia.meetapp.app.participants;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.PasswordNotMatchException;
import sonia.meetapp.exceptions.UserExistsException;

@Service
public class UserService {

    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public AppUser registerNewUser(NewAppUser newAppUser) {

        if (appUserRepo.findById(newAppUser.email()).isPresent()) {
            throw new UserExistsException(newAppUser.email());
        } else if (!newAppUser.password().equals(newAppUser.repeatPassword())) {
            throw new PasswordNotMatchException();
        }
        AppUser appUser = new AppUser(newAppUser.email(), passwordEncoder.encode(newAppUser.password()), newAppUser.contacts());
        return appUserRepo.save(appUser);
    }
}