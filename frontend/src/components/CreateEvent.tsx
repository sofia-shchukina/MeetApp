import {FormEvent, useState} from "react";
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import './AddNewParticipant.css';
import {toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {AppUser} from "../types/AppUser";


export default function CreateEvent(props:
                                        {
                                            addTheEvent: (name: string, place: string, time: string, description: string) => Promise<void>,
                                            appUser: AppUser | undefined,
                                        }) {

    const [errorMessage, setErrorMessage] = useState("");
    const [name, setName] = useState("");
    const [place, setPlace] = useState("");
    const [time, setTime] = useState("");
    const [description, setDescription] = useState("");

    const onFormSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!name || !place || !time || !description) {
            toast.error("All fields are mandatory")
        } else {
            props.appUser?.role === "admin" ?
                props.addTheEvent(name, place, time, description)
                    .then(() => {
                        setName("");
                        setPlace("");
                        setDescription("");
                        setTime("");
                        setErrorMessage("")
                    })
                    .catch((error) => {
                        setErrorMessage(error.response.data.message)
                    }) : setErrorMessage("You are not allowed to create events in this app")
        }
    }

    return (<>
            {props.appUser?.role === "admin" ?
                <form id="addEventForm" onSubmit={onFormSubmit}>
                    <label>Enter the name of event: </label>
                    <input className="newEventInput" value={name}
                           onChange={event => setName(event.target.value)}/>
                    <label>Enter the place of event: </label>
                    <input className="newEventInput" value={place}
                           onChange={event => setPlace(event.target.value)}/>
                    <label>Enter the time of event: </label>
                    <input className="newEventInput" value={time}
                           onChange={event => setTime(event.target.value)}/>
                    <label>Enter the description of event: </label>
                    <input className="newEventInput" value={description}
                           onChange={event => setDescription(event.target.value)}/>

                    <div className="errorMessage"> {errorMessage}</div>
                    <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
                </form> : <></>}
        </>
    );
}
