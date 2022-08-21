import {Button} from "@mui/material";
import {Participant} from "./Participant";
import DeleteIcon from '@mui/icons-material/Delete';
import './EachParticipant.css';
import {useNavigate} from "react-router-dom";

export default function EachParticipant(props:
                                            {
                                                participant: Participant
                                                deleteParticipant: (id: string) => Promise<void>,
                                            }) {
    const navigate = useNavigate();

    return (
        <li key={props.participant.id}>
            <div id="nameAndButton">
                <div className="nameStyle"> {props.participant.name} </div>
                <button onClick={() => {
                    navigate(`/participants/edit/${props.participant.id}`)
                }}>Edit
                </button>
                <Button variant="outlined" id="deletebtn" startIcon={<DeleteIcon id="deleteicon"/>}
                        onClick={() => props.deleteParticipant(props.participant.id)}>Delete</Button>

            </div>
        </li>
    )
}
