import {AppUser} from "../types/AppUser";

export default function EachUser(props: {

    user: AppUser,
}) {


    return (
        <div id="oneUser">
            <div id="name">{props.user.email}</div>
            <div id="contacts">{props.user.contacts}</div>
        </div>)
}
