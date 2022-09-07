import {FormEvent, useState} from "react";
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import './AddNewParticipant.css';
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {AppUser} from "../types/AppUser";


export default function AddNewParticipant(props:
                                              {
                                                  addParticipant: (name: string, email: string) => Promise<void>,
                                                  appUser: AppUser | undefined,
                                              }) {

    const [errorMessage, setErrorMessage] = useState("");
    const [name, setName] = useState("");

    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!name) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            props.appUser ?
                props.addParticipant(name, props.appUser.email)
                    .then(() => {
                        setName("");
                        setErrorMessage("")
                    })
                    .catch((error) => {
                        setErrorMessage(error.response.data.message)
                    })
                :
                setErrorMessage("there is no user logged in");
        }
    }

    return (
        <form id="registrationForm" onSubmit={onNameSubmit}>
            <label>What will be your name on the nametag at this event? </label>
            <input className="registrationInput" value={name}
                   onChange={event => setName(event.target.value)}/>
            <div className="errorMessage"> {errorMessage}</div>
            <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
        </form>
    );
}
