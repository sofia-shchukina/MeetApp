import {Participant} from "./Participant";
import {FormEvent, useState} from "react";
import {toast} from "react-toastify";
import {useNavigate, useParams} from "react-router-dom";
import SendIcon from "@mui/icons-material/Send";
import Button from "@mui/material/Button";
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import "./ParticipantDetails.css"

export default function ParticipantDetails(props: {
    participants: Participant[],
    editParticipant: (participantToEdit: Participant, editedName: string) => Promise<void>,
}) {
    const {id} = useParams();
    const participantToEdit = props.participants.find(item => item.id === id);
    const [editedName, setEditedName] = useState("");
    const navigate = useNavigate();

    if (!participantToEdit) {
        return <>
            Participant not found</>
    }
    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!editedName) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            props.editParticipant(participantToEdit, editedName).then(() => setEditedName("")).catch(() =>
                toast.error("Your name is not saved, please contact meetup host"))
        }
    }

    return (
        <form id="nameEditForm" onSubmit={onNameSubmit}>
            <label> How would you like to change your name?
                Your previous version was <>{participantToEdit.name}</>
            </label>
            <input id="detailsInput" value={editedName}
                   onChange={event => setEditedName(event.target.value)}/>
            <Button type="submit" id="saveButton" variant="contained" endIcon={<SendIcon/>}>save</Button>
            <Button type="submit" id="goBackButton" variant="contained" endIcon={<ArrowBackIosNewIcon/>}
                    onClick={() => {
                        navigate("/")
                    }}>back</Button>
        </form>
    );
}
