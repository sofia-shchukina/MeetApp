import {NavLink} from "react-router-dom";
import React from "react";
import './NavigationBar.css';

export default function NavigationBar() {

    return (
        <div className={"navigation"}>
            <NavLink to={"/"}>Registration</NavLink>
            <NavLink to={"/participants/likes/"}>Likes</NavLink>
            <NavLink to={"/participants/likes/analysis"}>Find Matches</NavLink>
        </div>
    )
}
