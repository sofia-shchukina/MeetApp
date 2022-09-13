import {AppUser} from "../types/AppUser";
import {TheEvent} from "../types/TheEvent";
import EachTheEvent from "./EachTheEvent";
import './EventGallery.css';

export default function EventGallery(props:
                                         {
                                             theEvents: TheEvent[]
                                             appUser: AppUser | undefined,
                                         }) {

    return (
        <div>
            <h3 id="listH3">Events</h3>
            <ul id="events">
                {props.theEvents.map(theEvent =>
                    <EachTheEvent key={theEvent.id}
                                  theEvent={theEvent}
                                  appUser={props.appUser}
                    />)}
            </ul>
        </div>
    );
}
