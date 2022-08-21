package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ParticipantsServiceTest {

    ParticipantsRepo participantsRepo = mock(ParticipantsRepo.class);
    Utility utility = mock(Utility.class);
    ParticipantsService participantsService = new ParticipantsService(participantsRepo, utility);

    @Test
    void getAllParticipants() {
        List<Participant> participants = List.of(
                new Participant("Alex", "12"),
                new Participant("Marina", "333"),
                new Participant("Ivan", "2222")
        );
        when(participantsRepo.findAll()).thenReturn(participants);
        List<Participant> actualResult = participantsService.getAllParticipants();
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
        Participant actualResult = participantsService.addParticipant(newParticipant);
        verify(participantsRepo).save(testParticipant);
        Assertions.assertEquals(testParticipant, actualResult);
    }

    @Test
    void deleteParticipant() {
        Participant testParticipant = new Participant("Daria", "54321");
        when(participantsRepo.existsById(testParticipant.getId())).thenReturn(true);
        doNothing().when(participantsRepo).deleteById(testParticipant.getId());
        participantsService.deleteParticipant(testParticipant.getId());
        verify(participantsRepo).deleteById(testParticipant.getId());
    }


    @Test
    void deleteParticipantDoesNotExist() {
        Participant testParticipant = new Participant("Daria", "54321");
        when(participantsRepo.existsById(testParticipant.getId())).thenReturn(false);
        String id = testParticipant.getId();
        try {
            participantsService.deleteParticipant(id);
            Assertions.fail("Expected exception was not thrown");
        } catch (ResponseStatusException ignored) {
        }
    }

    @Test
    void editParticipant() {
        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        Participant testParticipant = new Participant(newParticipant.getName(), id);

        when(participantsRepo.existsById(id)).thenReturn(true);
        doNothing().when(participantsRepo).deleteById(id);
        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);

        Participant actualResult = participantsService.editParticipant(id, newParticipant);
        verify(participantsRepo).deleteById(id);
        verify(participantsRepo).save(testParticipant);
        Assertions.assertEquals(testParticipant, actualResult);
    }

    @Test
    void editParticipantDoesNotExist() {
        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        Participant testParticipant = new Participant(newParticipant.getName(), id);

        when(participantsRepo.existsById(id)).thenReturn(false);
        try {
            participantsService.editParticipant(id, newParticipant);
            Assertions.fail("Expected exception was not thrown");
        } catch (ResponseStatusException ignored) {
        }
    }
}
