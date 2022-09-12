import {useEffect, useState} from "react";
import axios from "axios";
import {NewTheEvent, TheEvent} from "../types/TheEvent";

export default function useEvents() {
    const [theEvents, setTheEvents] = useState<TheEvent[]>([]);

    useEffect(() => {
        getAllTheEvents()
    }, [])

    const getAllTheEvents = () => {
        axios.get("/events")
            .then(response => response.data)
            .then(setTheEvents)
    }

    const addTheEvent = (name: string, place: string, time: string, description: string) => {
        const newTheEvent: NewTheEvent = {name, place, time, description}
        return axios.post("events", newTheEvent)
            .then(getAllTheEvents)
    }
    return {theEvents, addTheEvent}
}
