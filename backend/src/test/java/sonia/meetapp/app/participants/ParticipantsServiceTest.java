package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
}
