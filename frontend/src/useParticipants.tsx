import {useEffect, useState} from "react";
import {NewParticipant, Participant} from "./Participant";
import axios from "axios";
import {toast} from "react-toastify";
import {Like} from "./Like";

export default function useParticipants() {
    const [participants, setParticipants] = useState<Participant[]>([]);
    const [matches, setMatches] = useState<string[]>([]);

    useEffect(() => {
        getAllParticipants()
    }, [])

    const getAllParticipants = () => {
        axios.get("/participants")
            .then(response => response.data)
            .then(setParticipants)
    }

    const addParticipant = (name: string, email: string) => {
        const newParticipant: NewParticipant = {name, email}
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

    const editParticipant = (participantToEdit: Participant, editedName: string, email: string) => {
        const newParticipant: NewParticipant = {name: editedName, email}
        return axios.put("participants/edit/" + participantToEdit.id, newParticipant)
            .then(getAllParticipants)
    }

    const sendLike = (liker: Participant, liked: Participant[]) => {
        const like: Like = {likerID: liker.id, likedPeopleIDs: liked.map(participant => participant.id)}
        axios.put("participants/likes/", like)
            .catch(
                error => {
                    toast.error(error.message)
                })
    }

    const getAllMatches = (id: string) => {
        axios.get("/participants/likes/analysis/" + id)
            .then(response => response.data)
            .then(setMatches)
    }

    return {
        participants,
        addParticipant,
        deleteParticipant,
        editParticipant,
        sendLike,
        getAllMatches,
        matches,
    }
}
