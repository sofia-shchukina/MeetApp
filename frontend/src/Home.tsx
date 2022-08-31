import axios, {AxiosRequestConfig} from "axios";
import React, {useEffect, useState} from "react";
import './Home.css';
import NavigationBar from "./NavigationBar";
import {TextField} from "@mui/material";
import Button from "@mui/material/Button";


export default function Home() {
    const [user, setUser] = useState<string>()
    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const [message, setMessage] = useState<string>("")


    useEffect(() => {
        checkIfLogin()
    }, [])

    const config: AxiosRequestConfig = {
        headers: {
            Authorization: "Basic " + btoa(email + ":" + password)
        }
    }

    const checkIfLogin = () => {
        axios.get("/hello/me")
            .then((response) => {
                setUser(response.data)
            })
            .catch(() => {
                setUser(undefined)
            })
    }

    const login = () => {
        axios.get("/hello/login", config)
            .then((response) => {
                setUser(response.data)
                setMessage("you are logged in as ")
            })
    }

    const logout = () => {
        axios.get("/hello/logout")
            .then((response) => {
                setUser(undefined)
                checkIfLogin()
                setMessage("you were logged out. ")
            })
    }


    return (
        <div id="userLogin">


            {message}
            {user && user !== "anonymousUser" ? <>{user}</> : <>Already have an account? log in!</>}
            <TextField id="login" label="e-mail" color="warning" value={email}
                       onChange={(event) => setEmail(event.target.value)}/>
            <TextField id="logout" label="password" color="warning" type="password" value={password}
                       onChange={(event) => setPassword(event.target.value)}/>

            {user !== "anonymousUser" ?
                <Button type="submit" id="loginButton" variant="contained" onClick={logout}>Logout</Button> :
                <Button type="submit" id="logoutButton" variant="contained" onClick={login}>Login</Button>}
            {user !== "anonymousUser" ? <>{<NavigationBar/>}</> : <></>}
        </div>
    );
}
