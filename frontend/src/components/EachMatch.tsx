import {Participant} from "../types/Participant";
import {AppUser} from "../types/AppUser";


export default function EachMatch(props: {
    match: Participant
    appUsers: AppUser[],
}) {
    const appUser =
        props.appUsers.find(user => user.email === props.match.email);


    return (<>
        {props.match.name} <></>
        {appUser ? appUser.contacts : <>no contacts found</>}
    </>)

}