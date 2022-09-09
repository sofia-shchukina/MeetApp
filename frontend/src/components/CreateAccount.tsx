import {NewUser} from "../types/NewUser";
import React, {useState} from "react";
import {TextField} from "@mui/material";
import Button from "@mui/material/Button";
import './CreateAccount.css';
import {AxiosResponse} from "axios";
import {AppUser} from "../types/AppUser";


export default function CreateAccount(props:
                                          {
                                              appUser: AppUser | undefined,
                                              createUser: (newUser: NewUser) => Promise<AxiosResponse>
                                          }
) {
    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const [repeatPassword, setRepeatPassword] = useState<string>("")
    const [contacts, setContacts] = useState<string>("")
    const [message, setMessage] = useState<string>("")

    const handleSignUp = () => {
        const newUser: NewUser = {email, password, repeatPassword, contacts}
        props.createUser(newUser)
            .then(() => {
                setMessage("You were registered successfully")
            })
            .catch((error) => {
                setMessage(error.response.data.message)
            })
    }
    return (
        <>
            {props.appUser ? <></> :
                <form id="signUpForm">
                    <h3>New here? Sign up now!</h3>
                    <TextField id="login" label="e-mail" color="warning" value={email}
                               onChange={(event) => setEmail(event.target.value)}/>
                    <TextField id="logout" label="password" color="warning" type="password" value={password}
                               onChange={(event) => setPassword(event.target.value)}/>
                    <TextField id="logout" label="repeat password" color="warning" type="password"
                               value={repeatPassword}
                               onChange={(event) => setRepeatPassword(event.target.value)}/>
                    <TextField id="logout" label="contacts to share" color="warning" value={contacts}
                               onChange={(event) => setContacts(event.target.value)}/>
                    <Button type="submit" id="signUpButton" variant="contained" onClick={handleSignUp}>Sign Up!</Button>
                    <div id="message">{message}</div>
                </form>}
        </>
    )
}