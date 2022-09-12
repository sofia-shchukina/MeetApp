import {NavLink} from "react-router-dom";
import React from "react";
import './NavigationBar.css';

export default function NavigationBar() {

    return (
        <div className={"navigation"}>
            <NavLink to={"/"}>Home</NavLink>
            <NavLink to={"/events"}>Events</NavLink>
        </div>
    )
}
