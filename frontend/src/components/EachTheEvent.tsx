import {AppUser} from "../types/AppUser";
import {TheEvent} from "../types/TheEvent";
import {useNavigate} from "react-router-dom";
import './EachTheEvent.css';

export default function EachTheEvent(props:
                                         {
                                             theEvent: TheEvent
                                             appUser: AppUser | undefined,
                                         }) {

    const navigate = useNavigate();
    return (<>
            {props.appUser ?
                <li key={props.theEvent.id} id="eventLi">
                    <div id="oneEvent" onClick={() => {
                        navigate(`/events/${props.theEvent.id}`)
                    }}>
                        <div className="nameStyle"> {props.theEvent.name} </div>
                        <div className="placeStyle"> {props.theEvent.place} </div>
                        <div className="timeStyle"> {props.theEvent.time.toLocaleString()} </div>
                    </div>
                </li>
                : <></>}
        </>
    )
}
