import React from 'react';
import './App.css';
import useParticipants from "./useParticipants";
import ParticipantsList from "./ParticipantsList";
import AddNewParticipant from "./AddNewParticipant";
import {ToastContainer} from "react-toastify";
import {HashRouter, Route, Routes} from "react-router-dom";
import ParticipantDetails from "./ParticipantDetails";


export default function App() {

    const participantsHook = useParticipants()

    return (<>
            <HashRouter>
                <header>Speed-friending App</header>
                <main>
                    <h1>Registration</h1>
                    <Routes>
                        <Route path={"/"} element={
                            <>
                                <AddNewParticipant addParticipant={participantsHook.addParticipant}/>
                                <ParticipantsList participants={participantsHook.participants}
                                                  deleteParticipant={participantsHook.deleteParticipant}/>
                            </>
                        }/>
                        <Route path={"/participants/edit/:id"}
                               element={<ParticipantDetails participants={participantsHook.participants}
                                                            editParticipant={participantsHook.editParticipant}/>}/>
                    </Routes>

                </main>
            </HashRouter>
            <ToastContainer className="toast" position="top-center" style={{width: "150px"}}/>
        </>
    );
}
