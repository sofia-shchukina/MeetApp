import React from 'react';
import './App.css';
import useParticipants from "./useParticipants";
import ParticipantsList from "./ParticipantsList";
import AddNewParticipant from "./AddNewParticipant";
import {ToastContainer} from "react-toastify";


export default function App() {

    const participantsHook = useParticipants()

    return (<>
            <header>Speed-friending App</header>
            <h1>Registration</h1>
            <AddNewParticipant addParticipant={participantsHook.addParticipant}/>
            <ParticipantsList participants={participantsHook.participants}
                              deleteParticipant={participantsHook.deleteParticipant}/>
            <ToastContainer className="toast" position="top-center" style={{width: "150px"}}/>
        </>
    );
}
