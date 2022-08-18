import React from 'react';
import './App.css';
import useParticipants from "./useParticipants";
import ParticipantsList from "./ParticipantsList";


export default function App() {

    const participantsHook = useParticipants()

    return (<>
            <h1>Meetup Registration</h1>
            <ParticipantsList participants={participantsHook.participants}/>
        </>
    );

}
