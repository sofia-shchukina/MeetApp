import {Participant} from "../types/Participant";
import {AppUser} from "../types/AppUser";
import './EachMatch.css'


export default function EachMatch(props: {
    match: Participant
    appUsers: AppUser[],
}) {
    const appUser =
        props.appUsers.find(user => user.email === props.match.email);


    return (
        <div id="oneMatch">
            <div id="name">{props.match.name}</div>
            <div id="contacts">{appUser ? appUser.contacts : <>no contacts found</>}</div>
        </div>)
}
