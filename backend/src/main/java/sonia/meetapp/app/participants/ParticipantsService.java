package sonia.meetapp.app.participants;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.EmailIsNotUniqueException;
import sonia.meetapp.exceptions.NameIsNotUniqueException;
import sonia.meetapp.exceptions.NoPossibleCombinationsException;
import sonia.meetapp.exceptions.ParticipantNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class ParticipantsService {
    private static final String BREAK_PARTICIPANT_NAME = "break";
    private final ParticipantsRepo participantsRepo;
    private final Utility utility;

    public List<Participant> getAllParticipants() {
        return participantsRepo.findAll();
    }

    public Participant addParticipant(NewParticipant newParticipant) {
        Participant participant = new Participant(newParticipant.getName(), utility.createIdAsString(), newParticipant.getEmail());
        if (Boolean.TRUE.equals(thisNameIsUnique(newParticipant))) {
            if (Boolean.TRUE.equals(thisEmailIsUnique(newParticipant))) {
                return participantsRepo.save(participant);
            } else throw new EmailIsNotUniqueException();
        } else {
            throw new NameIsNotUniqueException();
        }
    }

    public void deleteParticipant(String id) {
        if (participantsRepo.existsById(id)) {
            participantsRepo.deleteById(id);
        } else {
            throw new ParticipantNotFoundException(id);
        }
    }

    public Participant editParticipant(String id, NewParticipant editedNewParticipant) {
        if (Boolean.TRUE.equals(thisNameIsUnique(editedNewParticipant))) {
            if (participantsRepo.existsById(id)) {
                return participantsRepo.save(new Participant(editedNewParticipant.getName(), id, editedNewParticipant.getEmail()));
            } else {
                throw new ParticipantNotFoundException(id);
            }
        } else {
            throw new NameIsNotUniqueException();
        }
    }

    public Boolean thisNameIsUnique(NewParticipant newParticipant) {
        String name = newParticipant.getName();
        List<Participant> allParticipants = participantsRepo.findAll();

        for (Participant allParticipant : allParticipants) {
            if (allParticipant.getName().toLowerCase(Locale.ROOT).equals(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public Boolean thisEmailIsUnique(NewParticipant newParticipant) {
        String email = newParticipant.getEmail();
        List<Participant> allParticipants = participantsRepo.findAll();
        if (!(allParticipants).isEmpty()) {
            for (Participant allParticipant : allParticipants) {
                if (allParticipant.getEmail().toLowerCase(Locale.ROOT).equals(email.toLowerCase())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Participant addLikes(Like like) {
        String likerID = like.getLikerID();
        List<String> likedPeopleIDsArrayList = new ArrayList<>(Arrays.asList(like.getLikedPeopleIDs()));

        Participant liker = participantsRepo.findById(likerID).orElseThrow(() -> new ParticipantNotFoundException(likerID));
        liker.setPeopleILike(likedPeopleIDsArrayList);

        for (String whoIsLikedID : likedPeopleIDsArrayList) {
            if (participantsRepo.existsById(whoIsLikedID)) {
                Participant likedPerson = participantsRepo.findById(whoIsLikedID).orElseThrow(() -> new ParticipantNotFoundException(whoIsLikedID));
                if (likedPerson.getPeopleWhoLikeMe() != null) {
                    likedPerson.getPeopleWhoLikeMe().add(likerID);
                } else {
                    likedPerson.setPeopleWhoLikeMe(new ArrayList<>(List.of(likerID)));
                }
                participantsRepo.save(likedPerson);
            }
        }
        return participantsRepo.save(liker);
    }

    public List<Participant> receiveMatches(String id) {
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

