import {AppUser} from "../types/AppUser";

export default function ManageUsers(props: {
    appUser: AppUser | undefined
}) {
    return (
        <>
            {props.appUser && props.appUser.role === "admin" ?
                <> Future admin page
                </>
                :
                <>
                    Hi, it's a page for app's admin.
                    If you can read it, that's because Sonia is just a newbie in the world of technology, tell her, that
                    you somehow came here, please:-)
                </>}

        </>)
}