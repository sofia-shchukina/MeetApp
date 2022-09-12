import {AppUser} from "../types/AppUser";
import {Participant} from "../types/Participant";
import EachUser from "./EachUser";

export default function ManageUsers(props: {
    appUser: AppUser | undefined,
    appUsers: AppUser[],
    participants: Participant[]
}) {
    return (
        <>
            {props.appUser && props.appUser.role === "admin" ?
                <div id="list">
                    <h3>List of Users</h3>
                    <ol>
                        {props.appUsers.map((user: AppUser) =>
                            <li>
                                <EachUser key={user.email}
                                          user={user}
                                          />
                            </li>
                        )}
                    </ol>
                </div>
                :
                <>
                    Nothing interesting here
                </>}

        </>)
}
