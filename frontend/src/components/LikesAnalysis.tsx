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
        //eslint-disable-next-line
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
        <div className="eventBigName"><h4>Receive matches</h4></div>
        <div className="clarificationText">Your name on meetup was {analyser ? analyser.name : <>unknown</>}
            . If it's incorrect, please contact the host. List of your matches is not final, it will be updated, as soon
            as other
            participants
            will send their likes. Come here later and press the button again:-)
        </div>
        <form className="likesForm">
            <Button type="submit" id="getMatchesButton" variant="contained" onClick={handleSubmit}> Get all
                matches </Button>
            <div className="list" id="listOfMatches">
                <h3> Your matches </h3>
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
