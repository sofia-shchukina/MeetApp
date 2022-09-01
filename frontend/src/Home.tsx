import {AxiosRequestConfig} from "axios";
import React, {useState} from "react";
import './Home.css';
import NavigationBar from "./NavigationBar";
import {TextField} from "@mui/material";
import Button from "@mui/material/Button";

export default function Home(props:
                                 {
                                     user: string | undefined,
                                     login: (config: AxiosRequestConfig) => void,
                                     logout: () => void,
                                     checkIfLogin: () => void,
                                 }) {

    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const config: AxiosRequestConfig = {
        headers: {
            Authorization: "Basic " + btoa(email + ":" + password)
        }
    }

    const handleLogin = () => {
        props.login(config)
    }
    return (
        <div id="userLogin">


            {props.user && props.user !== "anonymousUser" ? <>You are logged in as {props.user}</> : <>Already have an
                account? log in!</>}
            <TextField id="login" label="e-mail" color="warning" value={email}
                       onChange={(event) => setEmail(event.target.value)}/>
            <TextField id="logout" label="password" color="warning" type="password" value={password}
                       onChange={(event) => setPassword(event.target.value)}/>

            {props.user !== "anonymousUser" ?
                <Button type="submit" id="loginButton" variant="contained" onClick={props.logout}>Logout</Button> :
                <Button type="submit" id="logoutButton" variant="contained" onClick={handleLogin}>Login</Button>}
            {props.user !== "anonymousUser" ? <>{<NavigationBar/>}</> : <></>}
        </div>
    );
}
