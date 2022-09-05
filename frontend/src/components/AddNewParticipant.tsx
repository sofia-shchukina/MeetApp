import {FormEvent, useEffect, useState} from "react";
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import './AddNewParticipant.css';
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";


export default function AddNewParticipant(props:
                                              {
                                                  addParticipant: (name: string, email: string) => Promise<void>,
                                              }) {

    const [errorMessage, setErrorMessage] = useState("");
    const [name, setName] = useState("");
    const [user, setUser] = useState<string>()
    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!name) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            user ?
                props.addParticipant(name, user)
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
    useEffect(() => {
        checkIfLogin()
    }, [])
    const checkIfLogin = () => {
        axios.get("/hello/me")
            .then((response) => {
                setUser(response.data)
            })
            .catch(() => {
                setUser(undefined)
            })
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
