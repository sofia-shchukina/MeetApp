import {NavLink, useParams} from "react-router-dom";
import React from "react";
import './NavigationBar.css';

export default function NavigationBarInsideEvent() {
    const {eventId} = useParams();
    return (
        <div className={"navigation"}>
            <NavLink to={`/events/${eventId}`}>Event page</NavLink>
            <NavLink to={`/events/registration/${eventId}`}>Reserve a seat</NavLink>
            <NavLink to={`/events/pairs/${eventId}`}>Talk</NavLink>
            <NavLink to={`/events/likes/${eventId}`}>Send likes</NavLink>
            <NavLink to={`/events/likes/analysis/${eventId}`}>Receive matches</NavLink>
        </div>
    )
}