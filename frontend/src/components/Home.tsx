import {AxiosRequestConfig} from "axios";
import React, {useState} from "react";
import './Home.css';
import NavigationBar from "./NavigationBar";
import {TextField} from "@mui/material";
import Button from "@mui/material/Button";
import {AppUser} from "../types/AppUser";
import {useNavigate} from "react-router-dom";

export default function Home(props:
                                 {
                                     appUser: AppUser | undefined,
                                     login: (config: AxiosRequestConfig) => void,
                                     logout: () => void,
                                     checkIfLogin: () => void,
                                 }) {

    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const navigate = useNavigate();

    const config: AxiosRequestConfig = {
        headers: {
            Authorization: "Basic " + btoa(email + ":" + password)
        }
    }

    const handleLogin = () => {
        props.login(config)
        setEmail("")
        setPassword("")
    }
    return (
        <div id="userLogin">
            {props.appUser && props.appUser.email !== "anonymousUser" ?
                <>You are logged in as {props.appUser.email}</> :
                <>Already have an account? log in!</>}
            {props.appUser && props.appUser.email !== "anonymousUser" ?
                <Button type="submit" id="logoutButton" variant="contained" onClick={props.logout}>Logout</Button>
                :
                <>
                    <TextField id="login" label="e-mail" color="warning" value={email}
                               onChange={(event) => setEmail(event.target.value)}/>
                    <TextField id="logout" label="password" color="warning" type="password" value={password}
                               onChange={(event) => setPassword(event.target.value)}/>
                    <Button type="submit" id="loginButton" variant="contained" onClick={handleLogin}>Login</Button>
                </>
            }
            {props.appUser?.role === "admin" ?
                <Button type="submit" id="adminButton" variant="contained" onClick={() => {
                    navigate("/for-admin")
                }}>User-management</Button> : <></>}
            {props.appUser && props.appUser.email !== "anonymousUser" ? <>{<NavigationBar/>}</> : <></>}
        </div>
    );
}
