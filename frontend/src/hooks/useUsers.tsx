import axios, {AxiosRequestConfig} from "axios";
import {useEffect, useState} from "react";
import {NewUser} from "../types/NewUser";
import {AppUser} from "../types/AppUser";


export default function useUsers() {
    const [user, setUser] = useState<string>()

    useEffect(() => {
        checkIfLogin()
    }, [])

    const checkIfLogin = () => {
        axios.get("/hello/me")
            .then((response) => {
                setUser(response.data)
            })
            .catch(() => {
                setUser(undefined)
            })
    }

    const login = (config: AxiosRequestConfig) => {
        axios.get("/hello/login", config)
            .then((response) => {
                setUser(response.data)
            })

    }

    const logout = () => {
        axios.get("/hello/logout")
            .then(() => {
                setUser(undefined)
                checkIfLogin()
                localStorage.removeItem('matches');
            })
    }

    const createUser = (newUser: NewUser) => {
        return axios.post("/hello", newUser)

    }
    const [appUsers, setAppUsers] = useState<AppUser[]>([]);

    useEffect(() => {
        getAllAppUsers()
    }, [])

    const getAllAppUsers = () => {
        return axios.get("/hello/findUsers")
            .then(response => response.data)
            .then(setAppUsers)
    }

    return {user, login, logout, checkIfLogin, createUser, appUsers}
}
