package sonia.meetapp.app.participants;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepo appUserRepo;

    public AppUserDetailsService(AppUserRepo appUserRepo) {

        this.appUserRepo = appUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findById(username)
                .orElse(null);
        if (appUser == null) {
            return null;
        }
        return new User(appUser.email(), appUser.passwordHash(), Collections.emptyList());
    }
}
