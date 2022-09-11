import {AppUser} from "../types/AppUser";
import {TheEvent} from "../types/TheEvent";
import EachTheEvent from "./EachTheEvent";


export default function EventGallery(props:
                                         {
                                             theEvents: TheEvent[]
                                             appUser: AppUser | undefined,
                                         }) {

    return (
        <div id="list">
            <h3>List of Events</h3>
            <ol>
                {props.theEvents.map(theEvent =>
                    <EachTheEvent key={theEvent.id}
                                  theEvent={theEvent}
                                  appUser={props.appUser}
                    />)}
            </ol>
        </div>
    );
}