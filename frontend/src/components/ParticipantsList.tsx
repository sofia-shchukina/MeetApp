import EachParticipant from "./EachParticipant";
import {Participant} from "../types/Participant";
import "./ParticipantsList.css"
import {AppUser} from "../types/AppUser";
import {useEffect} from "react";
import {useParams} from "react-router-dom";

export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                                 deleteParticipant: (id: string, eventId: string) => Promise<void>,
                                                 appUser: AppUser | undefined,
                                                 getAllParticipants: (id: string) => void,
                                             }) {
    const {id} = useParams();
    useEffect(() => {
        props.getAllParticipants(id ? id : "fakeId")
        //eslint-disable-next-line
    }, [])
    return (
        <div id="list">
            <h3>List of participants</h3>
            <ol>
                {props.participants.map(participant =>
                    <EachParticipant key={participant.id}
                                     participant={participant}
                                     deleteParticipant={props.deleteParticipant}
                                     appUser={props.appUser}
                                     eventId={id}
                    />)}
            </ol>
        </div>
    );
}
