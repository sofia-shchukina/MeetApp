import {useEffect, useState} from "react";
import {NewParticipant, Participant} from "./Participant";
import axios from "axios";
import {toast} from "react-toastify";

export default function useParticipants() {
    const [participants, setParticipants] = useState<Participant[]>([]);
    useEffect(() => {
        getAllParticipants()
    }, [])

    const getAllParticipants = () => {
        axios.get("/participants")
            .then(response => response.data)
            .then(setParticipants)
    }

    const addParticipant = (name: string) => {
        const newParticipant: NewParticipant = {name}
        return axios.post("participants", newParticipant)
            .then(getAllParticipants)
    }

    const deleteParticipant = (id: string) => {
        return axios.delete("participants/" + id)
            .then(getAllParticipants)
            .catch(
                error => {
                    onErrorFunction(error)
                })
    }

    const onErrorFunction = (error: Error) => {
        toast.error(error.message)
    }

    return {participants, addParticipant, deleteParticipant}
}
