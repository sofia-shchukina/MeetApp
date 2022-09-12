import Button from "@mui/material/Button";
import './LikeAnalysis.css';
import {Participant} from "../types/Participant";
import {AppUser} from "../types/AppUser";
import EachMatch from "./EachMatch";
import {useParams} from "react-router-dom";
import {useEffect} from "react";

export default function LikesAnalysis(props: {
    participants: Participant[],
    getAllMatches: (id: string, eventId: string) => void,
    matches: Participant[],
    appUser: AppUser | undefined,
    appUsers: AppUser[],
    getAllParticipants: (id: string) => void,
}) {
    const {eventId} = useParams();
    useEffect(() => {
        props.getAllParticipants(eventId ? eventId : "fakeId")
    }, [])
    let email: string;
    if (props.appUser) {
        email = props.appUser.email
    }
    const analyser = props.participants.find(participant => participant.email === email);
    const handleSubmit = () => {
        if (analyser) props.getAllMatches(analyser.id, eventId ? eventId : "fakeId");
    }

    return <>
        <div className="clarificationText">Your name on meetup was {analyser ? analyser.name : <>unknown</>}
            . If it's incorrect, please contact the host to receive your matches :-)
        </div>
        <form className="likesForm">
            <Button type="submit" id="getMatchesButton" variant="contained" onClick={handleSubmit}> Get all
                matches </Button>
            <label> Here is a list of your matches, it is not final, it will be updated, as soon as other
                participants
                will send their likes. Come here later and press the button again:-) </label>
            <div id="listOfMatches">
                <h3> List of matches </h3>
                <ul>
                    {props.matches.length > 0 ? props.matches.map((match: Participant) =>
                            <li key={match.id}>
                                <EachMatch key={match.id}
                                           match={match}
                                           appUsers={props.appUsers}/>
                            </li>)
                        :
                        <h4>Your don't have any matches yet:-)
                        </h4>}
                </ul>
            </div>
        </form>
    </>
}
