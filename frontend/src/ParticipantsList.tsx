import {Participant} from "./Participant";
import "./ParticipantsList.css"

export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                             }) {

    return (
        <>
            <h2>List of participants</h2>
            <ol>
                {props.participants.map(participant =>
                    (<li key={participant.id}>{participant.name}</li>))}
            </ol>
        </>
    );
}
