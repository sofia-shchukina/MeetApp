package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParticipantsServiceTest {

    @Test
    void getAllParticipants() {
        List<Participant> participants = List.of(
                new Participant("1", "Alex"),
                new Participant("2", "Marina"),
                new Participant("3", "Ivan")
        );
        ParticipantsRepo participantsRepo = mock(ParticipantsRepo.class);
        when(participantsRepo.findAll()).thenReturn(participants);
        ParticipantsService participantService = new ParticipantsService(participantsRepo);

        List<Participant> actualResult = participantService.getAllParticipants();
        List<Participant> expectedResult = List.of(
                new Participant("1", "Alex"),
                new Participant("2", "Marina"),
                new Participant("3", "Ivan")
        );
        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }


    @Test
    void addParticipant() {

        String participantName = "Guillermo";
        String id = "123";
        Participant testParticipant = new Participant(participantName, id);
        ParticipantsRepo participantsRepo = mock(ParticipantsRepo.class);
        when(participantsRepo.save(any(Participant.class))).thenReturn(testParticipant);
        ParticipantsService participantsService = new ParticipantsService(participantsRepo);
        Participant actualResult = participantsService.addParticipant("Guillermo");
        assertThat(actualResult.name).isEqualTo(participantName);

    }
}