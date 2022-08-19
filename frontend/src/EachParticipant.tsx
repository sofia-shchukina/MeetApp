import {Button} from "@mui/material";
import {Participant} from "./Participant";
import DeleteIcon from '@mui/icons-material/Delete';
import './EachParticipant.css';

export default function EachParticipant(props:
                                            {
                                                participant: Participant
                                                deleteParticipant: (id: string) => Promise<void>,
                                            }) {
    return (
        <li key={props.participant.id}>
            <div id="nameAndButton">
                <div className="nameStyle"> {props.participant.name} </div>
                <Button variant="outlined" id="dltbtn" startIcon={<DeleteIcon id="deleteicon"/>}
                        onClick={() => props.deleteParticipant(props.participant.id)}/>
            </div>
        </li>
    )
}
