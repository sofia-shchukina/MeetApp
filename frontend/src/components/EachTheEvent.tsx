import {AppUser} from "../types/AppUser";
import {TheEvent} from "../types/TheEvent";

export default function EachTheEvent(props:
                                         {
                                             theEvent: TheEvent
                                             appUser: AppUser | undefined,
                                         }) {


    return (<>
            {props.appUser ?
                <li key={props.theEvent.id}>
                    <div id="nameAndButtons">
                        <div className="nameStyle"> {props.theEvent.name} </div>
                        <div className="placeStyle"> {props.theEvent.place} </div>
                        <div className="timeStyle"> {props.theEvent.time} </div>
                        <div className="descriptionStyle"> {props.theEvent.description} </div>
                    </div>
                </li>
                : <></>}
        </>
    )
}