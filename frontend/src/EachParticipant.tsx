import {Participant} from "./Participant";

export default function EachParticipant(props:
                                            {
                                                participant: Participant
                                                deleteParticipant: (id: string) => Promise<void>,
                                            }) {
    return (
        <li key={props.participant.id}>
            <div className={"nameStyle"}> {props.participant.name} </div>
            <button onClick={() => props.deleteParticipant(props.participant.id)}/>
        </li>
    )
}