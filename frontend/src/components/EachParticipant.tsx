import {Button} from "@mui/material";
import {Participant} from "../types/Participant";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import './EachParticipant.css';
import {useNavigate} from "react-router-dom";
import {AppUser} from "../types/AppUser";

export default function EachParticipant(props:
                                            {
                                                participant: Participant,
                                                deleteParticipant: (id: string) => Promise<void>,
                                                appUser: AppUser | undefined,
                                            }) {
    const navigate = useNavigate();

    return (
        <li key={props.participant.id}>
            <div id="nameAndButtons">
                <div className="nameStyle"> {props.participant.name} </div>
                {props.participant.email === props.appUser?.email || props.appUser?.role === "admin" ?
                    <div id="buttons">
                        <Button variant="outlined" id="personalButton"
                                startIcon={<EditIcon id="editIcon"/>}
                                onClick={() => {
                                    navigate(`/participants/edit/${props.participant.id}`)
                                }}>Edit
                        </Button>
                        <Button variant="outlined" id="personalButton" startIcon={<DeleteIcon id="deleteIcon"/>}
                                onClick={() => props.deleteParticipant(props.participant.id)}>Delete</Button>
                    </div>
                    : <></>}
            </div>
        </li>
    )
}
