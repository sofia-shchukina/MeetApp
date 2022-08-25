import {FormEvent, useState} from "react";
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import './AddNewParticipant.css';
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


export default function AddNewParticipant(props:
                                              {
                                                  addParticipant: (name: string) => Promise<void>,
                                              }) {

    const [errorMessage, setErrorMessage] = useState("");
    const [name, setName] = useState("");
    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!name) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            props.addParticipant(name)
                .then(() => {
                    setName("");
                    setErrorMessage("")
                })
                .catch((error) => {
                    setErrorMessage(error.response.data.message)
                });
        }
    }

    return (
        <form id="registrationForm" onSubmit={onNameSubmit}>
            <label>What is your name on the nametag? </label>
            <input className="registrationInput" value={name}
                   onChange={event => setName(event.target.value)}/>
            <div className="errorMessage"> {errorMessage}</div>
            <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
        </form>
    );
}
