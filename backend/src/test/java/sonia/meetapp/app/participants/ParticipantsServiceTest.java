package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sonia.meetapp.exceptions.EmailIsNotUniqueException;
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
                new Participant("Alex", "12", "alex@gmail.com"),
                new Participant("Marina", "333", "marina@gmail.com"),
                new Participant("Ivan", "2222", "ivan@gmail.com")
        );
        when(participantsRepo.findAll()).thenReturn(participants);
        List<Participant> actualResult = participantsService.getAllParticipants();
        List<Participant> expectedResult = List.of(
                new Participant("Alex", "12", "alex@gmail.com"),
                new Participant("Marina", "333", "marina@gmail.com"),
                new Participant("Ivan", "2222", "ivan@gmail.com")
        );
        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void addParticipant() {
        String participantName = "Guillermo";
        String id = "123";
        String email = "guillermo@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        newParticipant.setEmail(email);
        Participant testParticipant = new Participant(participantName, id, email);
        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);
        when(utility.createIdAsString()).thenReturn(id);
        Participant actualResult = participantsService.addParticipant(newParticipant);
        verify(participantsRepo).save(testParticipant);
        Assertions.assertEquals(testParticipant, actualResult);
    }

    @Test
    void addParticipantNotUniqueName() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234", "daniel@gmail.com"),
                new Participant("Guillermo", "1", "guillermo@gmail.com"));

        String participantName = "Guillermo";
        String id = "123";
        String email = "test@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        Participant testParticipant = new Participant(participantName, id, email);
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
    void addParticipantNotUniqueEmail() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234", "daniel@gmail.com"),
                new Participant("Guillermo", "1", "guillermo@gmail.com"));

        String participantName = "Frank";
        String id = "123";
        String email = "daniel@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        newParticipant.setEmail(email);
        Participant testParticipant = new Participant(participantName, id, email);
        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);
        when(participantsRepo.findAll()).thenReturn(testList);
        when(utility.createIdAsString()).thenReturn(id);
        try {
            participantsService.addParticipant(newParticipant);
            Assertions.fail("Expected exception was not thrown");
        } catch (EmailIsNotUniqueException ignored) {
        }
    }

    @Test
    void deleteParticipant() {
        Participant testParticipant = new Participant("Daria", "54321", "123@gmail.com");
        when(participantsRepo.existsById(testParticipant.getId())).thenReturn(true);
        doNothing().when(participantsRepo).deleteById(testParticipant.getId());
        participantsService.deleteParticipant(testParticipant.getId());
        verify(participantsRepo).deleteById(testParticipant.getId());
    }


    @Test
    void deleteParticipantDoesNotExist() {
        Participant testParticipant = new Participant("Daria", "54321", "123@gmail.com");
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
        String email = "123@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        newParticipant.setEmail("123@gmail.com");
        Participant testParticipant = new Participant(newParticipant.getName(), id, email);

        when(participantsRepo.existsById(id)).thenReturn(true);
        when(participantsRepo.save(testParticipant)).thenReturn(testParticipant);
        Participant actualResult = participantsService.editParticipant(id, newParticipant);

        verify(participantsRepo).save(testParticipant);
        Assertions.assertEquals(testParticipant, actualResult);
    }

    @Test
    void editParticipantNotUnique() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234", "daniel@gmail.com"),
                new Participant("George", "1", "daniel@gmail.com"));

        String id = "123";
        String email = "12@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        Participant testParticipant = new Participant(newParticipant.getName(), id, email);

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
        String email = "123@gmail.com";
        Participant testParticipant = new Participant(newParticipant.getName(), id, email);

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

        Participant dummieParticipant1 = new Participant("Florian", "123", "123@gmail.com");
        Participant dummieParticipant2 = new Participant("Daniel", "1234", "1234@gmail.com");
        when(participantsRepo.findAll()).thenReturn(List.of(dummieParticipant1, dummieParticipant2));

        Boolean actual = participantsService.thisNameIsUnique(newParticipant);
        Assertions.assertTrue(actual);
    }

    @Test
    void thisNameIsUniqueFalse() {
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");

        Participant dummieParticipant1 = new Participant("Florian", "123", "123@gmail.com");
        Participant dummieParticipant2 = new Participant("George", "1234", "123@gmail.com");
        when(participantsRepo.findAll()).thenReturn(List.of(dummieParticipant1, dummieParticipant2));

        Boolean actual = participantsService.thisNameIsUnique(newParticipant);
        Assertions.assertFalse(actual);
    }

    @Test
    void addLikes() {
        Participant dummieParticipant1 = new Participant("Florian", "123", "123@gmail.com");
        Participant dummieParticipant2 = new Participant("George", "1234", "123@gmail.com");
        Like like = new Like();
        like.setLikerID(dummieParticipant1.getId());
        String[] likedPeopleIDs = {"1234"};
        like.setLikedPeopleIDs(likedPeopleIDs);

        Participant expected = new Participant("Florian", "123", "123@gmail.com");
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
        Participant dummieParticipant1 = new Participant("Florian", "123", "123@gmail.com");
        Participant dummieParticipant2 = new Participant("George", "1234", "1234@gmail.com");
        Like like = new Like();
        like.setLikerID(dummieParticipant1.getId());
        String[] likedPeopleIDs = {"1234"};
        like.setLikedPeopleIDs(likedPeopleIDs);

        Participant expected = new Participant("Florian", "123", "123@gmail.com");
        expected.setPeopleILike(new ArrayList<>(List.of("1234")));

        when(participantsRepo.existsById(dummieParticipant1.getId())).thenReturn(false);
        try {
            participantsService.addLikes(like);
            Assertions.fail("Expected exception was not thrown");
        } catch (ParticipantNotFoundException ignored) {
        }
    }

    @Test
    void receiveMatches() {

        Participant participant1 = new Participant("Florian", "123", "123@gmail.com");
        Participant participant2 = new Participant("Dominic", "1234", "1234@gmail.com");
        Participant participant3 = new Participant("Christopher", "12345", "12345@gmail.com");
        participant1.setPeopleILike(new ArrayList<>(List.of(participant2.getId())));
        participant2.setPeopleILike(new ArrayList<>(List.of(participant1.getId())));
        participant1.setPeopleWhoLikeMe(new ArrayList<>(List.of(participant2.getId())));
        participant2.setPeopleWhoLikeMe(new ArrayList<>(List.of(participant1.getId())));
        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3));
        when(participantsRepo.findAll()).thenReturn(participants);
        when(participantsRepo.findById(participant1.getId())).thenReturn(Optional.of(participant1));

        List<Participant> expected = new ArrayList<>(List.of(
                participant2));
        List<Participant> actual = participantsService.receiveMatches(participant1.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs6Participants1Round() {
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");
        Participant participant6 = new Participant("F", "6", "12345@gmail.com");
        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5, participant6));
        when(participantsRepo.findAll()).thenReturn(participants);

        List<Participant> actual = participantsService.receivePairs();
        Assertions.assertEquals(participants, actual);
    }

    @Test
    void receivePairs6Participants2Round() {
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");
        Participant participant6 = new Participant("F", "6", "12345@gmail.com");

        participant1.setPeopleITalkedTo(new ArrayList<>(List.of(participant2.getId())));
        participant2.setPeopleITalkedTo(new ArrayList<>(List.of(participant1.getId())));
        participant3.setPeopleITalkedTo(new ArrayList<>(List.of(participant4.getId())));
        participant4.setPeopleITalkedTo(new ArrayList<>(List.of(participant3.getId())));
        participant5.setPeopleITalkedTo(new ArrayList<>(List.of(participant6.getId())));
        participant6.setPeopleITalkedTo(new ArrayList<>(List.of(participant5.getId())));

        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5, participant6));
        when(participantsRepo.findAll()).thenReturn(participants);

        List<Participant> expected = new ArrayList<>(List.of(participant1, participant3, participant2, participant5,
                participant4, participant6));
        List<Participant> actual = participantsService.receivePairs();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs6Participants3Round() {
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");
        Participant participant6 = new Participant("F", "6", "12345@gmail.com");

        participant1.setPeopleITalkedTo(new ArrayList<>(List.of(participant2.getId(), participant3.getId())));
        participant2.setPeopleITalkedTo(new ArrayList<>(List.of(participant1.getId(), participant5.getId())));
        participant3.setPeopleITalkedTo(new ArrayList<>(List.of(participant4.getId(), participant1.getId())));
        participant4.setPeopleITalkedTo(new ArrayList<>(List.of(participant3.getId(), participant6.getId())));
        participant5.setPeopleITalkedTo(new ArrayList<>(List.of(participant6.getId(), participant2.getId())));
        participant6.setPeopleITalkedTo(new ArrayList<>(List.of(participant5.getId(), participant3.getId())));

        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5, participant6));
        when(participantsRepo.findAll()).thenReturn(participants);

        List<Participant> expected = new ArrayList<>(List.of(participant1, participant4, participant2, participant6,
                participant3, participant5));
        List<Participant> actual = participantsService.receivePairs();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs5Participants1Round() {
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");


        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5));
        when(participantsRepo.findAll()).thenReturn(participants);

        List<Participant> actual = participantsService.receivePairs();
        Assertions.assertEquals(participants, actual);
    }

    @Test
    void receivePairs5Participants3Round() {
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");

        participant1.setPeopleITalkedTo(new ArrayList<>(List.of(participant2.getId(), participant3.getId())));
        participant2.setPeopleITalkedTo(new ArrayList<>(List.of(participant1.getId(), participant4.getId())));
        participant3.setPeopleITalkedTo(new ArrayList<>(List.of(participant4.getId(), participant1.getId())));
        participant4.setPeopleITalkedTo(new ArrayList<>(List.of(participant3.getId(), participant2.getId())));


        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5));
        when(participantsRepo.findAll()).thenReturn(participants);

        Participant participantBreak = new Participant("Break", "break", "break");
        participantBreak.setPeopleITalkedTo(new ArrayList<>(List.of(participant5.getId())));

        List<Participant> expected = new ArrayList<>(List.of(participant1, participant4, participant2, participant3,
                participant5, participantBreak));
        List<Participant> actual = participantsService.receivePairs();
        Assertions.assertEquals(expected, actual);
    }
}
