package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppUserDetailsServiceTest {

    AppUserRepo appUserRepo = mock(AppUserRepo.class);
    AppUserDetailsService appUserDetailsService = new AppUserDetailsService(appUserRepo);


    @Test
    void loadUserByUsername() {
        String email = "userName@gmail.com";
        AppUser appUser = new AppUser("userName@gmail.com", "12345");
        User user = new User("userName@gmail.com", "12345", Collections.emptyList());
        when(appUserRepo.findById(email)).thenReturn(Optional.of(appUser));
        User actual = (User) appUserDetailsService.loadUserByUsername(email);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void loadUserByUsernameFail() {
        String email = "userName@gmail.com";
        when(appUserRepo.findById(email)).thenReturn(Optional.empty());
        assertNull(appUserDetailsService.loadUserByUsername(email));
    }
}