package sonia.meetapp.app.participants;


import org.springframework.stereotype.Service;
import sonia.meetapp.exceptions.NameIsNotUniqueException;
import sonia.meetapp.exceptions.ParticipantNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Service
public class ParticipantsService {
    private final ParticipantsRepo participantsRepo;
    private final Utility utility;

    public ParticipantsService(ParticipantsRepo participantsRepo, Utility utility) {
        this.participantsRepo = participantsRepo;
        this.utility = utility;
    }

    public List<Participant> getAllParticipants() {
        return participantsRepo.findAll();
    }

    public Participant addParticipant(NewParticipant newParticipant) {
        Participant participant = new Participant(newParticipant.getName(), utility.createIdAsString());
        if (Boolean.TRUE.equals(thisNameIsUnique(newParticipant))) {
            return participantsRepo.save(participant);
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
                return participantsRepo.save(new Participant(editedNewParticipant.getName(), id));
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
}


