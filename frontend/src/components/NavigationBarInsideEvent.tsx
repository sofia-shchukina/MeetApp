import {NavLink, useParams} from "react-router-dom";
import React from "react";
import './NavigationBar.css';

export default function NavigationBarInsideEvent() {
    const {eventId} = useParams();
    console.log(eventId)
    return (
        <div className={"navigation"}>
            <NavLink to={`/events/${eventId}`}>Event</NavLink>
            <NavLink to={`/events/registration/${eventId}`}>RSVP</NavLink>
            <NavLink to={`/events/pairs/${eventId}`}>Talk</NavLink>
            <NavLink to={`/events/likes/${eventId}`}>Likes</NavLink>
            <NavLink to={`/events/likes/analysis/${eventId}`}>Matches</NavLink>
        </div>
    )
}
