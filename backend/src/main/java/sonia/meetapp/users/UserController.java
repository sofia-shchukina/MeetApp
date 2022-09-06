package sonia.meetapp.users;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("hello")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("login")
    String login() {
        return getUsername();
    }

    @GetMapping("me")
    String getUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @GetMapping("logout")
    void logout(HttpSession session) {
        session.invalidate();
    }

    @PostMapping()
    public ResponseEntity<AppUser> registerNewUser(@RequestBody NewAppUser newAppUser) {

        AppUser registeredUser = userService.registerNewUser(newAppUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @GetMapping("findUsers")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }
}
