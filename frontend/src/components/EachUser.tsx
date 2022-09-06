import {Participant} from "../types/Participant";
import {AppUser} from "../types/AppUser";

export default function EachUser(props: {
    participants: Participant[]
    user: AppUser,
}) {
    const appUser =
        props.participants.find(participant => participant.email === props.user.email);


    return (
        <div id="oneUser">
            <div id="name">{props.user.email}</div>
            <div id="contacts">{props.user.contacts}</div>
            <div id="participantName">{appUser ? "registered for the event as " + appUser.name :
                "is not registered for event"}</div>
        </div>)
}
