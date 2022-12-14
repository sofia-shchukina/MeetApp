import {useState} from "react";
import {NewParticipant, Participant} from "../types/Participant";
import axios from "axios";
import {toast} from "react-toastify";
import {Like} from "../types/Like";

export default function useParticipants() {
    const [participants, setParticipants] = useState<Participant[]>([]);
    const [matches, setMatches] = useState<Participant[]>([]);
    const [currentRound, setCurrentRound] = useState<Participant[][]>([]);

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

    const deleteParticipant = (id: string, eventId: string) => {
        return axios.delete(`participants/${eventId}/${id}`)
            .then(() => getAllParticipants(eventId))
            .catch(
                error => {
                    toast.error(error.message)
                })
    }

    const editParticipant = (participantToEdit: Participant, editedName: string, email: string, eventId: string) => {
        const newParticipant: NewParticipant = {name: editedName, email}
        return axios.put(`participants/edit/${eventId}/${participantToEdit.id}`, newParticipant)
            .then(() => getAllParticipants(eventId))
    }

    const sendLike = (liker: Participant, liked: Participant[], eventId: string) => {
        const like: Like = {likerID: liker.id, likedPeopleIDs: liked.map(participant => participant.id)}
        axios.put("participants/likes/" + eventId, like)
            .catch(
                error => {
                    toast.error(error.message)
                })
    }

    const getAllMatches = (participantId: string, eventId: string) => {
        axios.get(`/participants/likes/analysis/${eventId}/${participantId}`)
            .then(response => response.data)
            .then(setMatches)
    }

    const generatePairs = (eventId: string) => {
        return axios.get("/participants/pairs/" + eventId)
            .then(response => response.data)

    }
    const getCurrentRound = (eventId: string) => {
        axios.get(`/participants/pairs/${eventId}/currentRound`)
            .then(response => response.data)
            .then(setCurrentRound)
    }


    return {
        participants,
        addParticipant,
        deleteParticipant,
        editParticipant,
        sendLike,
        getAllMatches,
        matches,
        generatePairs,
        getAllParticipants,
        getCurrentRound,
        currentRound
    }
}
