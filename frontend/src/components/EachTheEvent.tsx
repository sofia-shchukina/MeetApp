import {AppUser} from "../types/AppUser";
import {TheEvent} from "../types/TheEvent";
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";

export default function EachTheEvent(props:
                                         {
                                             theEvent: TheEvent
                                             appUser: AppUser | undefined,
                                         }) {

    const navigate = useNavigate();

    return (<>
            {props.appUser ?
                <li key={props.theEvent.id}>
                    <div id="nameAndButtons">
                        <div className="nameStyle"> {props.theEvent.name} </div>
                        <div className="placeStyle"> {props.theEvent.place} </div>
                        <div className="timeStyle"> {props.theEvent.time.replace("T", " ")} </div>
                        <Button variant="outlined" id="personalButton"
                                onClick={() => {
                                    navigate(`/events/${props.theEvent.id}`)
                                }}>Check this event
                        </Button>
                    </div>
                </li>
                : <></>}
        </>
    )
}
