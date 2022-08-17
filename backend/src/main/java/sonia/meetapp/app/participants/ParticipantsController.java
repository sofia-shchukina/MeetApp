package sonia.meetapp.app.participants;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
public class ParticipantsController {

    @GetMapping("/hello")
    String sayHello() {

        return "Hello";
    }

}
