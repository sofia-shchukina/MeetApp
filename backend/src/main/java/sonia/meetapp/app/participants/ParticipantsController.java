package sonia.meetapp.app.participants;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantsController {

    private final ParticipantsService participantsService;

    public ParticipantsController(ParticipantsService participantsService) {
        this.participantsService = participantsService;
    }

    @GetMapping("/hello")
    String sayHello() {
        return "Hello";
    }


    @GetMapping()
    public List<Participant> getAllParticipants() {
        return participantsService.getAllParticipants();
    }
}
