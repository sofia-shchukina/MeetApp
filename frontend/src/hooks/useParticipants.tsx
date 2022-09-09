import {useState} from "react";
import {NewParticipant, Participant} from "../types/Participant";
import axios from "axios";
import {toast} from "react-toastify";
import {Like} from "../types/Like";

export default function useParticipants() {
    const [participants, setParticipants] = useState<Participant[]>([]);
    const [matches, setMatches] = useState<Participant[]>([]);
    const [pairs, setPairs] = useState<Participant[][]>([]);


    const getAllParticipants = (eventId: string) => {
        axios.get("/participants/" + eventId)
            .then(response => response.data)
            .then(setParticipants)
    }

    const addParticipant = (name: string, email: string, eventId: string) => {
        const newParticipant: NewParticipant = {name, email}
        return axios.post("participants/" + eventId, newParticipant)
            .then(() => getAllParticipants(eventId))
    }

    const deleteParticipant = (id: string) => {
        return axios.delete("participants/" + id)
            // .then(getAllParticipants)
            .catch(
                error => {
                    toast.error(error.message)
                })
    }

    const editParticipant = (participantToEdit: Participant, editedName: string, email: string) => {
        const newParticipant: NewParticipant = {name: editedName, email}
        return axios.put("participants/edit/" + participantToEdit.id, newParticipant)
        // .then(getAllParticipants)
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

    const getPairs = () => {
        return axios.get("/participants/pairs")
            .then(response => response.data)
            .then(setPairs)
    }


    return {
        participants,
        addParticipant,
        deleteParticipant,
        editParticipant,
        sendLike,
        getAllMatches,
        matches,
        getPairs,
        pairs, getAllParticipants
    }
}
