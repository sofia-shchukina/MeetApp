import {useEffect, useState} from "react";
import {NewParticipant, Participant} from "./Participant";
import axios from "axios";

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

    return {participants, addParticipant}

}