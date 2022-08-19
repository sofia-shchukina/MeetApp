import {Participant} from "./Participant";
import "./ParticipantsList.css"

export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                             }) {

    return (
        <div id="list">
            <h2>List of participants</h2>
            <ol>
                {props.participants.map(participant =>
                    (<li key={participant.id}>{participant.name}</li>))}
            </ol>
        </div>
    );
}
