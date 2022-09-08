import {NavLink} from "react-router-dom";
import React from "react";
import './NavigationBar.css';

export default function NavigationBar() {

    return (
        <div className={"navigation"}>
            <NavLink to={"/"}>Home</NavLink>
            <NavLink to={"/registration"}>Registration</NavLink>
            <NavLink to={"/pairs"}>Talk</NavLink>
            <NavLink to={"/participants/likes/"}>Likes</NavLink>
            <NavLink to={"/participants/likes/analysis"}>Matches</NavLink>
        </div>
    )
}
