import {Participant} from "./Participant";

export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                             }) {

    return (
        <>
            <h2>List of participants</h2>
            <ol>
                {props.participants.map(participant =>
                    (<li>{participant.name}</li>))}
            </ol>

        </>
    );
}
