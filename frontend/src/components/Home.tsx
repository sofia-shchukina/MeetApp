import {AxiosRequestConfig} from "axios";
import React, {useState} from "react";
import './Home.css';
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
        <body className="register">
        <div id="userLogin">
            {props.appUser && props.appUser.email !== "anonymousUser" ?
                <div id="commentText">You are logged in as {props.appUser.email}</div> :
                <h3>Already have an account? log in!</h3>}
            {props.appUser && props.appUser.email !== "anonymousUser" ?
                <Button type="submit" id="signUpButton" variant="contained" onClick={props.logout}>Logout</Button>
                :
                <>
                    <TextField id="login" label="e-mail" color="warning" value={email}
                               onChange={(event) => setEmail(event.target.value)}/>
                    <TextField id="login" label="password" color="warning" type="password" value={password}
                               onChange={(event) => setPassword(event.target.value)}/>
                    <Button type="submit" id="signUpButton" variant="contained" onClick={handleLogin}>Login</Button>
                </>
            }
            {props.appUser?.role === "admin" ?
                <Button type="submit" id="signUpButton" variant="contained" onClick={() => {
                    navigate("/for-admin")
                }}>User-management</Button> : <></>}
            {props.appUser ?
                <Button type="submit" id="checkEvents" variant="contained" onClick={() => {
                    navigate("/events")
                }}>Go check current events</Button> : <></>}
        </div>
        </body>
    );
}
