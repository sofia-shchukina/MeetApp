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
                    toast.error(error.message)
                })
    }

    const editParticipant = (participantToEdit: Participant, editedName: string) => {
        const newParticipant: NewParticipant = {name: editedName}
        return axios.put("participants/edit/" + participantToEdit.id, newParticipant)
            .then(getAllParticipants)
                }


    return {participants, addParticipant, deleteParticipant, editParticipant}
}
