import React, {useEffect, useState} from 'react';

import './App.css';
import axios from "axios";
import {Participant} from "./Participant";

export default function App() {

    const [message, setMessage] = useState();

    axios.get("/participants/hello")
        .then(response => response.data)
        .then(setMessage)

    const [participants, setParticipants] = useState<Participant[]>([]);
    useEffect(() => {
        getAllParticipants()
    }, [])

    const getAllParticipants = () => {
        axios.get("/participants")
            .then(response => response.data)
            .then(setParticipants)
    }

    return (<>
            <h1>{message}</h1>
            <h2>{participants.map(participant =>
                participant.name)}</h2>
        </>
    );
}
