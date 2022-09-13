import {useEffect, useState} from "react";
import axios from "axios";
import {NewTheEvent, TheEvent} from "../types/TheEvent";

export default function useEvents() {
    const [theEvents, setTheEvents] = useState<TheEvent[]>([]);

    useEffect(() => {
        getAllTheEvents()
    }, [])

    const getAllTheEvents = () => {
        axios.get<TheEvent[]>("/events")
            .then(response => response.data)
            .then((data) => data.map((theEvent) => {
                    return {...theEvent, time: new Date(theEvent.time)}
                }
            ))
            .then(setTheEvents)
    }

    const addTheEvent = (name: string, place: string, time: Date, description: string) => {
        const newTheEvent: NewTheEvent = {name, place, time, description}
        return axios.post("events", newTheEvent)
            .then(getAllTheEvents)
    }
    return {theEvents, addTheEvent}
}
