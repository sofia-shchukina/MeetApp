package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sonia.meetapp.exceptions.NameIsNotUniqueException;
import sonia.meetapp.exceptions.ParticipantNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    void addParticipantNotUniqueName() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234"),
                new Participant("Guillermo", "1"));

        String participantName = "Guillermo";
        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        Participant testParticipant = new Participant(participantName, id);
        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);
        when(participantsRepo.findAll()).thenReturn(testList);
        when(utility.createIdAsString()).thenReturn(id);
        try {
            participantsService.addParticipant(newParticipant);
            Assertions.fail("Expected exception was not thrown");
        } catch (NameIsNotUniqueException ignored) {
        }
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
        } catch (ParticipantNotFoundException ignored) {
        }
    }

    @Test
    void editParticipant() {
        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        Participant testParticipant = new Participant(newParticipant.getName(), id);

        when(participantsRepo.existsById(id)).thenReturn(true);

        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);


        Participant actualResult = participantsService.editParticipant(id, newParticipant);

        verify(participantsRepo).save(testParticipant);
        Assertions.assertEquals(testParticipant, actualResult);
    }

    @Test
    void editParticipantNotUnique() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234"),
                new Participant("George", "1"));


        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        Participant testParticipant = new Participant(newParticipant.getName(), id);

        when(participantsRepo.findAll()).thenReturn(testList);
        when(participantsRepo.existsById(id)).thenReturn(true);
        doNothing().when(participantsRepo).deleteById(id);
        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);

        try {
            participantsService.editParticipant(id, newParticipant);
            Assertions.fail("Expected exception was not thrown");
        } catch (NameIsNotUniqueException ignored) {
        }
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
        } catch (ParticipantNotFoundException ignored) {
        }
    }

    @Test
    void thisNameIsUnique() {
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");

        Participant dummieParticipant1 = new Participant("Florian", "123");
        Participant dummieParticipant2 = new Participant("Daniel", "1234");
        when(participantsRepo.findAll()).thenReturn(List.of(dummieParticipant1, dummieParticipant2));

        Boolean actual = participantsService.thisNameIsUnique(newParticipant);
        Assertions.assertTrue(actual);
    }

    @Test
    void thisNameIsUniqueFalse() {
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");

        Participant dummieParticipant1 = new Participant("Florian", "123");
        Participant dummieParticipant2 = new Participant("George", "1234");
        when(participantsRepo.findAll()).thenReturn(List.of(dummieParticipant1, dummieParticipant2));

        Boolean actual = participantsService.thisNameIsUnique(newParticipant);
        Assertions.assertFalse(actual);
    }

    @Test
    void addLikes() {
        Participant dummieParticipant1 = new Participant("Florian", "123");
        Participant dummieParticipant2 = new Participant("George", "1234");
        Like like = new Like();
        like.setLikerID(dummieParticipant1.getId());
        String[] likedPeopleIDs = {"1234"};
        like.setLikedPeopleIDs(likedPeopleIDs);

        Participant expected = new Participant("Florian", "123");
        expected.setPeopleILike(new ArrayList<>(List.of("1234")));

        when(participantsRepo.existsById(dummieParticipant1.getId())).thenReturn(true);
        when(participantsRepo.save(expected)).thenReturn(expected);
        when(participantsRepo.findById(dummieParticipant1.getId())).thenReturn(Optional.of(dummieParticipant1));
        when(participantsRepo.findById(dummieParticipant2.getId())).thenReturn(Optional.of(dummieParticipant2));
        Participant actual = participantsService.addLikes(like);
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void addLikesDoesNotExist() {
        Participant dummieParticipant1 = new Participant("Florian", "123");
        Participant dummieParticipant2 = new Participant("George", "1234");
        Like like = new Like();
        like.setLikerID(dummieParticipant1.getId());
        String[] likedPeopleIDs = {"1234"};
        like.setLikedPeopleIDs(likedPeopleIDs);

        Participant expected = new Participant("Florian", "123");
        expected.setPeopleILike(new ArrayList<>(List.of("1234")));

        when(participantsRepo.existsById(dummieParticipant1.getId())).thenReturn(false);

        try {
            participantsService.addLikes(like);
            Assertions.fail("Expected exception was not thrown");
        } catch (ParticipantNotFoundException ignored) {
        }
    }
}
