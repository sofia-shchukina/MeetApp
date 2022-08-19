package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ParticipantsServiceTest {


    ParticipantsRepo participantsRepo = mock(ParticipantsRepo.class);
    Utility utility = mock(Utility.class);
    ParticipantsService participantService = new ParticipantsService(participantsRepo, utility);

    @Test
    void getAllParticipants() {
        List<Participant> participants = List.of(
                new Participant("Alex", "12"),
                new Participant("Marina", "333"),
                new Participant("Ivan", "2222")
        );

        when(participantsRepo.findAll()).thenReturn(participants);
        List<Participant> actualResult = participantService.getAllParticipants();
        List<Participant> expectedResult = List.of(
                new Participant("Alex", "12"),
                new Participant("Marina", "333"),
                new Participant("Ivan", "2222")
        );
        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void addParticipant() {

        String participantName = "Guillermo";
        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        Participant testParticipant = new Participant(participantName, id);

        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);
        when(utility.createIdAsString()).thenReturn(id);

        Participant actualResult = participantService.addParticipant(newParticipant);
        verify(participantsRepo).save(testParticipant);
        Assertions.assertEquals(testParticipant, actualResult);
    }
}

