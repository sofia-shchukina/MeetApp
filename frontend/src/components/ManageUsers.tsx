import {AppUser} from "../types/AppUser";

export default function ManageUsers(props: {
    appUser: AppUser | undefined
}) {
    return (
        <>
            {props.appUser && props.appUser.role === "admin" ? <> admin </> : <> just user </>}

            <h3>hallo</h3>
        </>)
}