import {Participant} from "../types/Participant";
import {FormEvent, useEffect, useState} from "react";
import {toast} from "react-toastify";
import {useNavigate, useParams} from "react-router-dom";
import SendIcon from "@mui/icons-material/Send";
import Button from "@mui/material/Button";
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import "./ParticipantDetails.css"
import {AppUser} from "../types/AppUser";

export default function ParticipantDetails(props: {
    getAllParticipants: (id: string) => void,
    appUser: AppUser | undefined,
    participants: Participant[],
    editParticipant: (participantToEdit: Participant, editedName: string, email: string, eventId: string) => Promise<void>,
}) {
    useEffect(() => {
        props.getAllParticipants(eventId ? eventId : "fakeId")
        //eslint-disable-next-line
    }, [])
    const [errorMessage, setErrorMessage] = useState("");
    const {id} = useParams();
    const {eventId} = useParams();
    const participantToEdit = props.participants.find(item => item.id === id);
    const [editedName, setEditedName] = useState("");

    const navigate = useNavigate();


    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!editedName) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            props.appUser && participantToEdit ?
                props.editParticipant(participantToEdit, editedName, props.appUser.email, eventId ? eventId : "fakeId").then(() => {
                    setEditedName("");
                    setErrorMessage("")
                })
                    .catch((error) => {
                        setErrorMessage(error.response.data.message)
                    }) :
                setErrorMessage("there is no user logged in");
        }
    }

    return (
        <>
            <div className="eventBigName"><h4>Reserve a seat</h4></div>
            <form id="nameEditForm" onSubmit={onNameSubmit}>
                <label> How would you like to change your name?
                    Your previous version was <>{participantToEdit ? participantToEdit.name : ""}</>
                </label>
                <input id="detailsInput" value={editedName}
                       onChange={event => setEditedName(event.target.value)}/>
                <div className="errorMessage"> {errorMessage}</div>
                <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
                <Button type="submit" id="goBackButton" variant="contained" endIcon={<ArrowBackIosNewIcon/>}
                        onClick={() => {
                            navigate("/events/registration/" + eventId)
                        }}>back</Button>
            </form>
        </>
    );
}
