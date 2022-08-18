import React from 'react';
import './App.css';
import useParticipants from "./useParticipants";
import ParticipantsList from "./ParticipantsList";


export default function App() {

    const participantsHook = useParticipants()

    return (<>
            <header>Speed-friending App</header>
            <h1>Registration</h1>
            <ParticipantsList participants={participantsHook.participants}/>
        </>
    );

}
