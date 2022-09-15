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
    const {eventId} = useParams();
    useEffect(() => {
        props.getAllParticipants(eventId ? eventId : "fakeId")
        //eslint-disable-next-line
    }, [])
    return (<>
            <div className="list" id="participantList">
                <h3>Already registered</h3>
                <ol>
                    {props.participants.map(participant =>
                        <EachParticipant key={participant.id}
                                         participant={participant}
                                         deleteParticipant={props.deleteParticipant}
                                         appUser={props.appUser}
                                         eventId={eventId}
                        />)}
                </ol>
            </div>
        </>
    );
}
