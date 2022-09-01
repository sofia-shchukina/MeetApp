import React from 'react';
import './App.css';
import useParticipants from "./useParticipants";
import ParticipantsList from "./ParticipantsList";
import AddNewParticipant from "./AddNewParticipant";
import {ToastContainer} from "react-toastify";
import {HashRouter, Route, Routes} from "react-router-dom";
import ParticipantDetails from "./ParticipantDetails";
import NavigationBar from './NavigationBar';
import LikesCollection from './LikesCollection';
import LikesAnalysis from './LikesAnalysis';
import Home from "./Home";
import useUsers from './useUsers';


export default function App() {

    const participantsHook = useParticipants()
    const userHook = useUsers();

    return (<>
            <HashRouter>
                <header>Speed-Friending</header>
                <main>

                    <Routes>
                        <Route path={"/"} element={<Home user={userHook.user}
                                                         login={userHook.login}
                                                         logout={userHook.logout}
                                                         checkIfLogin={userHook.checkIfLogin}/>}/>
                        <Route path={"/registration"} element={
                            <>
                                <AddNewParticipant addParticipant={participantsHook.addParticipant}/>
                                <ParticipantsList participants={participantsHook.participants}
                                                  deleteParticipant={participantsHook.deleteParticipant}
                                                  user={userHook.user}/>
                                <NavigationBar/>
                            </>
                        }/>
                        <Route path={"/participants/edit/:id"}
                               element={
                                   <>
                                       <ParticipantDetails participants={participantsHook.participants}
                                                           editParticipant={participantsHook.editParticipant}/>
                                       <NavigationBar/>
                                   </>
                               }/>
                        <Route path={"/participants/likes/"}
                               element={
                                   <>
                                       <LikesCollection sendLike={participantsHook.sendLike}
                                                        participants={participantsHook.participants}
                                                        user={userHook.user}
                                       />
                                       <NavigationBar/>
                                   </>}/>
                        <Route path={"/participants/likes/analysis"}
                               element={
                                   <>
                                       <LikesAnalysis
                                           participants={participantsHook.participants}
                                           getAllMatches={participantsHook.getAllMatches}
                                           matches={participantsHook.matches}
                                           user={userHook.user}/>
                                       <NavigationBar/>
                                   </>}/>
                    </Routes>
                </main>
            </HashRouter>
            <ToastContainer className="toast" position="top-center" style={{width: "150px"}}/>
        </>
    );
}
