import EachParticipant from "./EachParticipant";
import {Participant} from "./Participant";
import "./ParticipantsList.css"


export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                                 deleteParticipant: (id: string) => Promise<void>,
                                             }) {

    return (
        <div id="list">
            <h2>List of participants</h2>
            <ol>
                {props.participants.map(participant =>
                    <EachParticipant key={participant.id} participant={participant}
                                     deleteParticipant={props.deleteParticipant}/>)}
            </ol>
        </div>
    );
}
