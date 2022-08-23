import {NavLink} from "react-router-dom";
import React from "react";

export default function NavigationBar() {

    return (
        <div className={"navigation"}>
            <NavLink to={"/"}>Registration</NavLink>
            <NavLink to={"/likes"}>Likes</NavLink>
        </div>
    )
}
