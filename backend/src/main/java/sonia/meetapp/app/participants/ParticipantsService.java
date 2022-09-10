package sonia.meetapp.app.participants;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sonia.meetapp.events.Event;
import sonia.meetapp.events.EventRepo;
import sonia.meetapp.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class ParticipantsService {
    private static final String BREAK_PARTICIPANT_NAME = "break";
    private final ParticipantsRepo participantsRepo;
    private final EventRepo eventRepo;
    private final Utility utility;

    public List<Participant> getAllParticipants(String eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        return event.getEventParticipants();
    }

    public Participant addParticipant(NewParticipant newParticipant, String eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Participant participant = new Participant(newParticipant.getName(), utility.createIdAsString(), newParticipant.getEmail());
        if (Boolean.TRUE.equals(thisNameIsUnique(newParticipant, event))) {
            if (Boolean.TRUE.equals(thisEmailIsUnique(newParticipant, event))) {
                event.getEventParticipants().add(participant);
                eventRepo.save(event);
                return participant;
            } else throw new EmailIsNotUniqueException();
        } else {
            throw new NameIsNotUniqueException();
        }
    }

    public void deleteParticipant(String eventId, String participantId) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Participant toDelete = event.getEventParticipants().stream().filter(participant ->
                participant.getId().equals(participantId)).findFirst().orElseThrow(() ->
                new ParticipantNotFoundException(participantId));
        event.getEventParticipants().remove(toDelete);
        eventRepo.save(event);
    }

    public Participant editParticipant(String participantId, String eventId, NewParticipant editedNewParticipant) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (Boolean.TRUE.equals(thisNameIsUnique(editedNewParticipant, event))) {
            Participant toEdit = event.getEventParticipants().stream().filter(participant ->
                    participant.getId().equals(participantId)).findFirst().orElseThrow(() ->
                    new ParticipantNotFoundException(participantId));
            event.getEventParticipants().remove(toEdit);
            toEdit.setName(editedNewParticipant.getName());
            event.getEventParticipants().add(toEdit);
            eventRepo.save(event);
            return toEdit;
        } else {
            throw new NameIsNotUniqueException();
        }
    }

    public Boolean thisNameIsUnique(NewParticipant newParticipant, Event event) {
        String name = newParticipant.getName();
        List<Participant> allParticipants = event.getEventParticipants();
        for (Participant allParticipant : allParticipants) {
            if (allParticipant.getName().toLowerCase(Locale.ROOT).equals(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public Boolean thisEmailIsUnique(NewParticipant newParticipant, Event event) {
        String email = newParticipant.getEmail();
        List<Participant> allParticipants = event.getEventParticipants();
        if (!(allParticipants).isEmpty()) {
            for (Participant allParticipant : allParticipants) {
                if (allParticipant.getEmail().toLowerCase(Locale.ROOT).equals(email.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Participant addLikes(Like like, String eventId) {
        String likerID = like.getLikerID();
        List<String> likedPeopleIDsArrayList = new ArrayList<>(Arrays.asList(like.getLikedPeopleIDs()));

        Event event = eventRepo.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Participant liker = event.getEventParticipants().stream().filter(participant ->
                participant.getId().equals(likerID)).findFirst().orElseThrow(() ->
                new ParticipantNotFoundException(likerID));
        event.getEventParticipants().remove(liker);
        liker.setPeopleILike(likedPeopleIDsArrayList);

        for (String whoIsLikedID : likedPeopleIDsArrayList) {

            Participant likedPerson = event.getEventParticipants().stream().filter(participant ->
                    participant.getId().equals(whoIsLikedID)).findFirst().orElseThrow(() ->
                    new ParticipantNotFoundException(whoIsLikedID));
            event.getEventParticipants().remove(likedPerson);
            if (likedPerson.getPeopleWhoLikeMe() != null) {
                likedPerson.getPeopleWhoLikeMe().add(likerID);
            } else {
                likedPerson.setPeopleWhoLikeMe(new ArrayList<>(List.of(likerID)));
            }
            event.getEventParticipants().add(likedPerson);
        }
        event.getEventParticipants().add(liker);
        eventRepo.save(event);
        return liker;
    }

    public List<Participant> receiveMatches(String eventId, String participantId) {
        Participant participant = participantsRepo.findById(id).orElseThrow(() -> new ParticipantNotFoundException(id));
        List<Participant> allParticipants = participantsRepo.findAll();
        List<Participant> matches = new ArrayList<>();
        if (participant.getPeopleWhoLikeMe() != null && participant.getPeopleILike() != null) {
            for (String idParticipantILike : participant.getPeopleILike()) {
                if (participant.getPeopleWhoLikeMe().contains(idParticipantILike)) {
                    Participant matchParticipant = allParticipants.stream().filter(item -> item.getId().equals(idParticipantILike)).findFirst().orElseThrow(() -> new ParticipantNotFoundException(idParticipantILike));
                    matches.add(matchParticipant);
                }
            }
        }
        return matches;
    }

    public List<Participant> receivePairs() {
        List<Participant> allParticipants = participantsRepo.findAll();
        if (allParticipants.size() % 2 == 1) {
            Participant breakParticipant = new Participant(BREAK_PARTICIPANT_NAME, BREAK_PARTICIPANT_NAME, BREAK_PARTICIPANT_NAME);
            allParticipants.add(breakParticipant);
            participantsRepo.save(breakParticipant);
        }

        List<Participant> generatedPairs = new ArrayList<>();
        for (int i = 0; i < allParticipants.size(); i++) {
            generatedPairs.add(null);
        }
        if (solve(allParticipants, generatedPairs)) {
            for (int i = 0; i < generatedPairs.size(); i++) {

                int finalI = i;
                Participant participantToEdit = participantsRepo.findById(generatedPairs.get(i).getId()).orElseThrow(() -> new ParticipantNotFoundException(generatedPairs.get(finalI).getId()));

                if (i % 2 == 0) {

                    if (participantToEdit.getPeopleITalkedTo() != null) {
                        participantToEdit.getPeopleITalkedTo().add(generatedPairs.get(i + 1).getId());

                    } else {
                        participantToEdit.setPeopleITalkedTo(new ArrayList<>(List.of(generatedPairs.get(i + 1).getId())));
                    }
                } else {
                    if (participantToEdit.getPeopleITalkedTo() != null) {
                        participantToEdit.getPeopleITalkedTo().add(generatedPairs.get(i - 1).getId());
                    } else {
                        participantToEdit.setPeopleITalkedTo(new ArrayList<>(List.of(generatedPairs.get(i - 1).getId())));
                    }
                }
                participantsRepo.save(participantToEdit);
            }
            participantsRepo.deleteById(BREAK_PARTICIPANT_NAME);
            return generatedPairs;
        } else {
            participantsRepo.deleteById(BREAK_PARTICIPANT_NAME);
            throw new
                    NoPossibleCombinationsException();
        }
    }

    public static boolean solve(List<Participant> allParticipants, List<Participant> generatedPairs) {

        for (int personPlace = 0; personPlace < allParticipants.size(); personPlace++) {
            if (generatedPairs.get(personPlace) == null) {
                for (int participantToTry = 0; participantToTry < allParticipants.size(); participantToTry++) {
                    if (isValidPlacement(generatedPairs, allParticipants.get(participantToTry), personPlace)) {
                        generatedPairs.set(personPlace, allParticipants.get(participantToTry));
                        if (solve(allParticipants, generatedPairs)) {
                            return true;
                        } else {
                            generatedPairs.set(personPlace, null);
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPlacement(List<Participant> generatedPairs, Participant participantToTry,
                                           int personPlace) {
        if (generatedPairs.contains(participantToTry)) {
            return false;
        } else {
            if (personPlace % 2 == 0) {
                return true;
            } else {
                if ((participantToTry.getPeopleITalkedTo() == null) && (generatedPairs.get(personPlace - 1).getPeopleITalkedTo() == null)) {
                    return true;
                } else if ((participantToTry.getPeopleITalkedTo() != null) && (generatedPairs.get(personPlace - 1).getPeopleITalkedTo() != null)) {
                    return !generatedPairs.get(personPlace - 1).getPeopleITalkedTo().contains(participantToTry.getId()) &&
                            !participantToTry.getPeopleITalkedTo().contains(generatedPairs.get(personPlace - 1).getId());
                } else if ((participantToTry.getPeopleITalkedTo() == null) && (generatedPairs.get(personPlace - 1).getPeopleITalkedTo() != null)) {
                    return !generatedPairs.get(personPlace - 1).getPeopleITalkedTo().contains(participantToTry.getId());
                } else {
                    return !participantToTry.getPeopleITalkedTo().contains(generatedPairs.get(personPlace - 1).getId());
                }
            }
        }
    }
}

