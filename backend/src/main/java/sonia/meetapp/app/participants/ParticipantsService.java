package sonia.meetapp.app.participants;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.EmailIsNotUniqueException;
import sonia.meetapp.exceptions.NameIsNotUniqueException;
import sonia.meetapp.exceptions.ParticipantNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class ParticipantsService {
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
}
