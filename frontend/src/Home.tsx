import axios, {AxiosRequestConfig} from "axios";
import {useEffect, useState} from "react";
import './Home.css';


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
        <div id="login">
            {message}
            {user !== "anonymousUser" ? <>{user}</> : <>Already have an account? log in!</>}
            <input value={email} onChange={(event) => setEmail(event.target.value)}/>
            <input type="password" value={password} onChange={(event) => setPassword(event.target.value)}/>
            {user !== "anonymousUser" ? <button onClick={logout}>Logout</button> :
                <button onClick={login}>Login</button>}
        </div>
    );
}
