import React from 'react';
import './App.css';
import useParticipants from "./useParticipants";
import ParticipantsList from "./ParticipantsList";
import AddNewParticipant from "./AddNewParticipant";


export default function App() {

    const participantsHook = useParticipants()

    return (<>
            <header>Speed-friending App</header>
            <h1>Registration</h1>
            <AddNewParticipant addParticipant={participantsHook.addParticipant}/>
            <ParticipantsList participants={participantsHook.participants}/>
        </>
    );

}
