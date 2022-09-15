import axios, {AxiosRequestConfig} from "axios";
import {useEffect, useState} from "react";
import {NewUser} from "../types/NewUser";
import {AppUser} from "../types/AppUser";


export default function useUsers() {
    const [appUser, setAppUser] = useState<AppUser>()

    useEffect(() => {
        checkIfLogin()
    }, [])

    const checkIfLogin = () => {
        axios.get("/hello/me")
            .then((response) => {
                setAppUser(response.data)
            })
            .catch(() => {
                setAppUser(undefined)
            })
    }

    const login = (config: AxiosRequestConfig) => {
        axios.get("/hello/login", config)
            .then((response) => {
                setAppUser(response.data)
            })
    }

    const logout = () => {
        axios.get("/hello/logout")
            .then(() => {
                setAppUser(undefined)
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

    return {appUser, login, logout, checkIfLogin, createUser, appUsers}
}
