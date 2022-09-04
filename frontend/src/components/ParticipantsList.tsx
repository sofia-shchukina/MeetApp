import EachParticipant from "./EachParticipant";
import {Participant} from "../types/Participant";
import "./ParticipantsList.css"

export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                                 deleteParticipant: (id: string) => Promise<void>,
                                                 user: string | undefined,
                                             }) {

    return (
        <div id="list">
            <h3>List of participants</h3>
            <ol>
                {props.participants.map(participant =>
                    <EachParticipant key={participant.id}
                                     participant={participant}
                                     deleteParticipant={props.deleteParticipant}
                                     user={props.user}
                    />)}
            </ol>
        </div>
    );
}
