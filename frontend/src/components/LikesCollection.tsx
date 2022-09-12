import {Participant} from "../types/Participant";
import {ChangeEvent, FormEvent, useEffect, useState} from "react";
import './LikesCollection.css';
import SendIcon from "@mui/icons-material/Send";
import Button from "@mui/material/Button";
import {AppUser} from "../types/AppUser";
import {useParams} from "react-router-dom";

export default function LikesCollection(props:
                                            {
                                                participants: Participant[],
                                                sendLike: (liker: Participant, liked: Participant[], eventId: string) => void,
                                                appUser: AppUser | undefined,
                                                getAllParticipants: (id: string) => void,
                                            }) {

    const [likedNames, setLikedNames] = useState<string[]>([]);
    const [resultMessage, setResultMessage] = useState<string>("");
    const {eventId} = useParams();

    useEffect(() => {
        props.getAllParticipants(eventId ? eventId : "fakeId")
    }, [])

    const handleLikedChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.currentTarget.checked) {
            setLikedNames([...likedNames, event.target.value])
        } else {
            const updatedLikedNames = likedNames.filter((name) => {
                return name !== event.target.value
            });
            setLikedNames(updatedLikedNames);
        }
    }
    let email: string;
    if (props.appUser) {
        email = props.appUser.email
    }
    const likerParticipant = props.participants.find(participant => participant.email === email);
    const handleSubmitAForm = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        let likedParticipants: Participant[] = [];
        likedNames.forEach((name: string) => {
            const likedParticipant = props.participants.find(participant => participant.name === name);
            if (likedParticipant) {
                likedParticipants.push(likedParticipant)
            }
        })
        if (likerParticipant && likedParticipants.length > 0) {
            props.sendLike(likerParticipant, likedParticipants, eventId ? eventId : "fakeId");
            setLikedNames([])
            setResultMessage("Your likes were succesfully submitted, you'll receive contacts of your matches soon");
        } else {
            setResultMessage("Your likes were not submitted, please check, that you liked minimum one person")
        }
    }

    return (
        <>
            <div className="clarificationText">Your name on meetup
                was {likerParticipant ? likerParticipant.name : <>unknown</>}
                . If it's incorrect, please contact the host before sending likes :-)
            </div>
            <form className="likesForm" onSubmit={handleSubmitAForm}>
                <label> Check here all people you liked </label>
                <div id="checkboxes">
                    {props.participants.map((participant) =>
                        (<div id="onePersonCheck" key={participant.name}>
                            <input type="checkbox" className="checkbox" key={participant.name}
                                   onChange={handleLikedChange}
                                   value={participant.name}
                            ></input>
                            <label> {participant.name}</label>
                        </div>))}
                </div>
                <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
                <div id="resultMessage">{resultMessage} </div>
            </form>
        </>
    );
}
