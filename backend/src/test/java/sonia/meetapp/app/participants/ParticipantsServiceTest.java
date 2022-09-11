package sonia.meetapp.app.participants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sonia.meetapp.events.Event;
import sonia.meetapp.events.EventRepo;
import sonia.meetapp.exceptions.EmailIsNotUniqueException;
import sonia.meetapp.exceptions.NameIsNotUniqueException;
import sonia.meetapp.exceptions.NoPossibleCombinationsException;
import sonia.meetapp.exceptions.ParticipantNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ParticipantsServiceTest {

    EventRepo eventRepo = mock(EventRepo.class);
    Utility utility = mock(Utility.class);
    ParticipantsService participantsService = new ParticipantsService(eventRepo, utility);
    String eventId = "123";
    Event mockedEvent = new Event("123", "Speed-Friending", "park", "today", "only for girls");


    @Test
    void getAllParticipants() {
        List<Participant> testList = List.of(
                new Participant("Alex", "12", "alex@gmail.com"),
                new Participant("Marina", "333", "marina@gmail.com"),
                new Participant("Ivan", "2222", "ivan@gmail.com"));

        mockedEvent.setEventParticipants(testList);

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        List<Participant> actualResult = participantsService.getAllParticipants(eventId);
        List<Participant> expectedResult = List.of(
                new Participant("Alex", "12", "alex@gmail.com"),
                new Participant("Marina", "333", "marina@gmail.com"),
                new Participant("Ivan", "2222", "ivan@gmail.com")
        );
        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void addParticipant() {
        mockedEvent.setEventParticipants(new ArrayList<>());

        String participantName = "Guillermo";
        String id = "123";
        String email = "guillermo@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        newParticipant.setEmail(email);

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant testParticipant = new Participant(participantName, id, email);
        when(utility.createIdAsString()).thenReturn(id);
        when(eventRepo.save(mockedEvent)).thenReturn(mockedEvent);
        Participant actualResult = participantsService.addParticipant(newParticipant, eventId);

        Assertions.assertEquals(testParticipant, actualResult);
    }

    @Test
    void addParticipantNotUniqueName() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234", "daniel@gmail.com"),
                new Participant("Guillermo", "1", "123"));

        mockedEvent.setEventParticipants(testList);

        String participantName = "Guillermo";
        String id = "123";
        String email = "guillermo@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        newParticipant.setEmail(email);

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        when(utility.createIdAsString()).thenReturn(id);
        when(eventRepo.save(mockedEvent)).thenReturn(mockedEvent);
        try {
            participantsService.addParticipant(newParticipant, eventId);
            Assertions.fail("Expected exception was not thrown");
        } catch (NameIsNotUniqueException ignored) {
        }
    }

    @Test
    void addParticipantNotUniqueEmail() {
        List<Participant> testList = List.of(
                new Participant("Daniel", "1234", "daniel@gmail.com"),
                new Participant("Alex", "1", "guillermo@gmail.com"));

        mockedEvent.setEventParticipants(testList);

        String participantName = "Guillermo";
        String id = "123";
        String email = "guillermo@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName(participantName);
        newParticipant.setEmail(email);

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        when(utility.createIdAsString()).thenReturn(id);
        when(eventRepo.save(mockedEvent)).thenReturn(mockedEvent);
        try {
            participantsService.addParticipant(newParticipant, eventId);
            Assertions.fail("Expected exception was not thrown");
        } catch (EmailIsNotUniqueException ignored) {
        }
    }

    @Test
    void deleteParticipant() {
        Participant testParticipant = new Participant("Alex", "1", "guillermo@gmail.com");
        Participant testParticipant2 = new Participant("Daniel", "1234", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant);
        testList.add(testParticipant2);

        mockedEvent.setEventParticipants(testList);

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        when(eventRepo.save(mockedEvent)).thenReturn(mockedEvent);

        participantsService.deleteParticipant(mockedEvent.getId(), testParticipant.getId());
        verify(eventRepo).save(mockedEvent);
        Assertions.assertEquals(1, mockedEvent.getEventParticipants().size());
    }


    @Test
    void deleteParticipantDoesNotExist() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        List<Participant> testList = new ArrayList<>();

        mockedEvent.setEventParticipants(testList);
        Participant testParticipant = new Participant("Daria", "54321", "123@gmail.com");
        try {
            participantsService.deleteParticipant(mockedEvent.getId(), testParticipant.getId());
            Assertions.fail("Expected exception was not thrown");
        } catch (ParticipantNotFoundException ignored) {
        }
    }

    @Test
    void editParticipant() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        Participant testParticipant = new Participant("Alex", "1", "guillermo@gmail.com");
        Participant testParticipant2 = new Participant("Daniel", "123", "123@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        String id = "123";
        String email = "123@gmail.com";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");
        newParticipant.setEmail("123@gmail.com");
        Participant endParticipant = new Participant(newParticipant.getName(), id, email);

        Participant actualResult = participantsService.editParticipant(id, eventId, newParticipant);

        Assertions.assertEquals(endParticipant, actualResult);
    }

    @Test
    void editParticipantNotUnique() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant testParticipant1 = new Participant("Daniel", "1234", "daniel@gmail.com");
        Participant testParticipant2 = new Participant("George", "1", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant1);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        String id = "123";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");

        try {
            participantsService.editParticipant(id, eventId, newParticipant);
            Assertions.fail("Expected exception was not thrown");
        } catch (NameIsNotUniqueException ignored) {
        }
    }

    @Test
    void editParticipantDoesNotExist() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant testParticipant1 = new Participant("Daniel", "1234", "daniel@gmail.com");
        Participant testParticipant2 = new Participant("George", "1", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant1);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        String id = "12";
        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("Go");

        try {
            participantsService.editParticipant(id, eventId, newParticipant);
            Assertions.fail("Expected exception was not thrown");
        } catch (ParticipantNotFoundException ignored) {
        }
    }

    @Test
    void thisNameIsUnique() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant testParticipant1 = new Participant("Daniel", "1234", "daniel@gmail.com");
        Participant testParticipant2 = new Participant("Alex", "1", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant1);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("George");

        Boolean actual = participantsService.thisNameIsUnique(newParticipant, mockedEvent);
        Assertions.assertTrue(actual);
    }

    @Test
    void thisNameIsUniqueFalse() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant testParticipant1 = new Participant("Daniel", "1234", "daniel@gmail.com");
        Participant testParticipant2 = new Participant("Alex", "1", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant1);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        NewParticipant newParticipant = new NewParticipant();
        newParticipant.setName("Alex");

        Boolean actual = participantsService.thisNameIsUnique(newParticipant, mockedEvent);
        Assertions.assertFalse(actual);
    }

    @Test
    void addLikes() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        Participant testParticipant1 = new Participant("Daniel", "1234", "daniel@gmail.com");
        Participant testParticipant2 = new Participant("Alex", "1", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant1);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        Like like = new Like();
        like.setLikerID(testParticipant1.getId());
        String[] likedPeopleIDs = {"1"};
        like.setLikedPeopleIDs(likedPeopleIDs);

        Participant expected = new Participant("Daniel", "1234", "daniel@gmail.com");
        expected.setPeopleILike(new ArrayList<>(List.of("1")));

        Participant actual = participantsService.addLikes(like, eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addLikesDoesNotExist() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        Participant testParticipant1 = new Participant("Daniel", "1234", "daniel@gmail.com");
        Participant testParticipant2 = new Participant("Alex", "1", "daniel@gmail.com");
        List<Participant> testList = new ArrayList<>();
        testList.add(testParticipant1);
        testList.add(testParticipant2);
        mockedEvent.setEventParticipants(testList);

        Like like = new Like();
        like.setLikerID("111");
        String[] likedPeopleIDs = {"1"};
        like.setLikedPeopleIDs(likedPeopleIDs);

        try {
            participantsService.addLikes(like, eventId);
            Assertions.fail("Expected exception was not thrown");
        } catch (ParticipantNotFoundException ignored) {
        }
    }

    @Test
    void receiveMatches() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        Participant participant1 = new Participant("Florian", "123", "123@gmail.com");
        Participant participant2 = new Participant("Dominic", "1234", "1234@gmail.com");
        Participant participant3 = new Participant("Christopher", "12345", "12345@gmail.com");
        participant1.setPeopleILike(new ArrayList<>(List.of(participant2.getId())));
        participant2.setPeopleILike(new ArrayList<>(List.of(participant1.getId())));
        participant1.setPeopleWhoLikeMe(new ArrayList<>(List.of(participant2.getId())));
        participant2.setPeopleWhoLikeMe(new ArrayList<>(List.of(participant1.getId())));
        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3));
        mockedEvent.setEventParticipants(participants);

        List<Participant> expected = new ArrayList<>(List.of(
                participant2));
        List<Participant> actual = participantsService.receiveMatches(participant1.getId(), eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs6Participants1Round() {

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");
        Participant participant6 = new Participant("F", "6", "12345@gmail.com");
        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5, participant6));
        mockedEvent.setEventParticipants(participants);

        List<Participant> expected1couple = new ArrayList<>(List.of(participant1, participant2));
        List<Participant> expected2couple = new ArrayList<>(List.of(participant3, participant4));
        List<Participant> expected3couple = new ArrayList<>(List.of(participant5, participant6));

        List<List<Participant>> expected = new ArrayList<>(List.of(expected1couple, expected2couple, expected3couple));

        List<List<Participant>> actual = participantsService.receivePairs(eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs6Participants2Round() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
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
        mockedEvent.setEventParticipants(participants);

        List<Participant> expected1couple = new ArrayList<>(List.of(participant1, participant3));
        List<Participant> expected2couple = new ArrayList<>(List.of(participant2, participant5));
        List<Participant> expected3couple = new ArrayList<>(List.of(participant4, participant6));

        List<List<Participant>> expected = new ArrayList<>(List.of(expected1couple, expected2couple, expected3couple));

        List<List<Participant>> actual = participantsService.receivePairs(eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs6Participants3Round() {

        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
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
        mockedEvent.setEventParticipants(participants);

        List<Participant> expected1couple = new ArrayList<>(List.of(participant1, participant4));
        List<Participant> expected2couple = new ArrayList<>(List.of(participant2, participant6));
        List<Participant> expected3couple = new ArrayList<>(List.of(participant3, participant5));

        List<List<Participant>> expected = new ArrayList<>(List.of(expected1couple, expected2couple, expected3couple));

        List<List<Participant>> actual = participantsService.receivePairs(eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs5Participants1Round() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));
        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");
        Participant participantBreak = new Participant("break", "break", "break");

        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5));
        mockedEvent.setEventParticipants(participants);

        participantBreak.setPeopleITalkedTo(new ArrayList<>(List.of(participant5.getId())));
        List<Participant> expected1couple = new ArrayList<>(List.of(participant1, participant2));
        List<Participant> expected2couple = new ArrayList<>(List.of(participant3, participant4));
        List<Participant> expected3couple = new ArrayList<>(List.of(participant5, participantBreak));

        List<List<Participant>> expected = new ArrayList<>(List.of(expected1couple, expected2couple, expected3couple));

        List<List<Participant>> actual = participantsService.receivePairs(eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs5Participants3Round() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");
        Participant participant4 = new Participant("D", "4", "123@gmail.com");
        Participant participant5 = new Participant("E", "5", "1234@gmail.com");
        Participant participantBreak = new Participant("break", "break", "break");

        participant1.setPeopleITalkedTo(new ArrayList<>(List.of(participant2.getId(), participant3.getId())));
        participant2.setPeopleITalkedTo(new ArrayList<>(List.of(participant1.getId(), participant4.getId())));
        participant3.setPeopleITalkedTo(new ArrayList<>(List.of(participant4.getId(), participant1.getId())));
        participant4.setPeopleITalkedTo(new ArrayList<>(List.of(participant3.getId(), participant2.getId())));


        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3, participant4,
                participant5));
        mockedEvent.setEventParticipants(participants);

        participantBreak.setPeopleITalkedTo(new ArrayList<>(List.of(participant5.getId())));
        List<Participant> expected1couple = new ArrayList<>(List.of(participant1, participant4));
        List<Participant> expected2couple = new ArrayList<>(List.of(participant2, participant3));
        List<Participant> expected3couple = new ArrayList<>(List.of(participant5, participantBreak));

        List<List<Participant>> expected = new ArrayList<>(List.of(expected1couple, expected2couple, expected3couple));

        List<List<Participant>> actual = participantsService.receivePairs(eventId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void receivePairs3ParticipantsNoCombination() {
        when(eventRepo.findById("123")).thenReturn(Optional.of(mockedEvent));

        Participant participant1 = new Participant("A", "1", "123@gmail.com");
        Participant participant2 = new Participant("B", "2", "1234@gmail.com");
        Participant participant3 = new Participant("C", "3", "12345@gmail.com");

        participant1.setPeopleITalkedTo(new ArrayList<>(List.of(participant2.getId())));
        participant2.setPeopleITalkedTo(new ArrayList<>(List.of(participant3.getId())));
        participant3.setPeopleITalkedTo(new ArrayList<>(List.of(participant1.getId())));


        List<Participant> participants = new ArrayList<>(List.of(participant1, participant2, participant3));
        mockedEvent.setEventParticipants(participants);
        try {
            participantsService.receivePairs(eventId);
            Assertions.fail("Expected exception was not thrown");
        } catch (NoPossibleCombinationsException ignored) {
        }

    }
}
