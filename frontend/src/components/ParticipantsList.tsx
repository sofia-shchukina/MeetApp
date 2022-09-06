import EachParticipant from "./EachParticipant";
import {Participant} from "../types/Participant";
import "./ParticipantsList.css"
import {AppUser} from "../types/AppUser";

export default function ParticipantsList(props:
                                             {
                                                 participants: Participant[],
                                                 deleteParticipant: (id: string) => Promise<void>,
                                                 appUser: AppUser | undefined,
                                             }) {

    return (
        <div id="list">
            <h3>List of participants</h3>
            <ol>
                {props.participants.map(participant =>
                    <EachParticipant key={participant.id}
                                     participant={participant}
                                     deleteParticipant={props.deleteParticipant}
                                     appUser={props.appUser}
                    />)}
            </ol>
        </div>
    );
}
