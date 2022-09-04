import {Participant} from "../types/Participant";
import {FormEvent, useEffect, useState} from "react";
import {toast} from "react-toastify";
import {useNavigate, useParams} from "react-router-dom";
import SendIcon from "@mui/icons-material/Send";
import Button from "@mui/material/Button";
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import "./ParticipantDetails.css"
import axios from "axios";

export default function ParticipantDetails(props: {
    participants: Participant[],
    editParticipant: (participantToEdit: Participant, editedName: string, email: string) => Promise<void>,
}) {
    const [errorMessage, setErrorMessage] = useState("");
    const {id} = useParams();
    const participantToEdit = props.participants.find(item => item.id === id);
    const [editedName, setEditedName] = useState("");
    const [user, setUser] = useState<string>()
    const navigate = useNavigate();

    useEffect(() => {
        checkIfLogin()
    }, [])
    if (!participantToEdit) {
        return <>
            Participant not found</>
    }

    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!editedName) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            user ?
                props.editParticipant(participantToEdit, editedName, user).then(() => {
                    setEditedName("");
                    setErrorMessage("")
                })
                    .catch((error) => {
                        setErrorMessage(error.response.data.message)
                    }) :
                setErrorMessage("there is no user logged in");
        }
    }

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
        <form id="nameEditForm" onSubmit={onNameSubmit}>
            <label> How would you like to change your name?
                Your previous version was <>{participantToEdit.name}</>
            </label>
            <input id="detailsInput" value={editedName}
                   onChange={event => setEditedName(event.target.value)}/>
            <div className="errorMessage"> {errorMessage}</div>
            <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
            <Button type="submit" id="goBackButton" variant="contained" endIcon={<ArrowBackIosNewIcon/>}
                    onClick={() => {
                        navigate("/registration")
                    }}>back</Button>
        </form>
    );
}
