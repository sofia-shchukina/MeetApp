import {NavLink} from "react-router-dom";
import React from "react";
import './NavigationBar.css';

export default function NavigationBar() {

    return (
        <div className={"navigation"}>
            <NavLink to={"/"}>To login page</NavLink>
            <NavLink to={"/events"}>Check events</NavLink>
        </div>
    )
}
