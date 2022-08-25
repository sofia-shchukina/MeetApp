import {Participant} from "./Participant";
import {ChangeEvent, FormEvent, useEffect, useState} from "react";
import './LikesCollection.css';
import SendIcon from "@mui/icons-material/Send";
import Button from "@mui/material/Button";

export default function LikesCollection(props:
                                            {
                                                participants: Participant[],
                                                sendLike: (liker: Participant, liked: Participant[]) => void,
                                            }) {


    const [likerName, setLikerName] = useState<string>("");
    const [likedNames, setLikedNames] = useState<string[]>([]);
    const [resultMessage, setResultMessage] = useState<string>("");
    const handleLikerChange = (event: ChangeEvent<HTMLSelectElement>) => {
        setLikerName(event.target.value);
    }

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

    useEffect(() => {
        if (props.participants && props.participants.length > 0) {
            setLikerName(props.participants[0].name);
        }
    }, [props.sendLike, (props.participants)])


    const handleSubmitAForm = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const likerParticipant = props.participants.find(participant => participant.name === likerName);
        let likedParticipants: Participant[] = [];
        likedNames.forEach((name: string) => {
            const likedParticipant = props.participants.find(participant => participant.name === name);
            if (likedParticipant) {
                likedParticipants.push(likedParticipant)
            }
        })
        if (likerParticipant && likedParticipants.length > 0) {
            props.sendLike(likerParticipant, likedParticipants);
            setLikedNames([])
            setResultMessage("Your likes were succesfully submitted, you'll receive contacts of your matches soon");
        } else {
            setResultMessage("Your likes were not submitted, please check, that you liked minimum one person")
        }
    }

    return (
        <form id="likesForm" onSubmit={handleSubmitAForm}>
            <label> What was your name on the Meetup? </label>
            <select value={likerName} onChange={handleLikerChange}>
                {props.participants.map((participant) =>
                    (<option className="option" key={participant.name}
                             value={participant.name}>{participant.name}</option>))}
            </select>
            <label> Check here all people you liked </label>
            <div id="checkboxes">
                {props.participants.map((participant) =>
                    (<div id="onePersonCheck">
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

    );
}