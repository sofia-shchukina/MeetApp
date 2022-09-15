import React from 'react';
import './App.css';
import useParticipants from "./hooks/useParticipants";
import ParticipantsList from "./components/ParticipantsList";
import AddNewParticipant from "./components/AddNewParticipant";
import {ToastContainer} from "react-toastify";
import {HashRouter, Route, Routes} from "react-router-dom";
import NavigationBar from './components/NavigationBar';
import LikesCollection from './components/LikesCollection';
import LikesAnalysis from './components/LikesAnalysis';
import Home from "./components/Home";
import useUsers from './hooks/useUsers';
import CreateAccount from './components/CreateAccount';
import ManageUsers from "./components/ManageUsers";
import PairGeneration from "./components/PairGeneration";
import FirstPage from "./components/FirstPage";
import useEvents from "./hooks/useEvents";
import CreateEvent from './components/CreateEvent';
import EventGallery from "./components/EventGallery";
import TheEventPage from "./components/TheEventPage";
import ParticipantDetails from './components/ParticipantDetails';
import NavigationBarInsideEvent from "./components/NavigationBarInsideEvent";

export default function App() {

    const participantsHook = useParticipants()
    const userHook = useUsers();
    const eventHook = useEvents();

    return (<>
            <HashRouter>
                <main>
                    <Routes>
                        <Route path={"/"}
                               element={
                                       <FirstPage/>
                               }/>
                        <Route path={"/create-account"}
                               element={
                                   <CreateAccount createUser={userHook.createUser}
                                                  appUser={userHook.appUser}/>
                               }/>
                        <Route path={"/login"}
                               element={
                                   <Home appUser={userHook.appUser}
                                         login={userHook.login}
                                         logout={userHook.logout}
                                         checkIfLogin={userHook.checkIfLogin}/>
                               }/>
                        <Route path={"/for-admin"}
                               element={<ManageUsers appUser={userHook.appUser}
                                                     appUsers={userHook.appUsers}
                                                     participants={participantsHook.participants}/>}/>
                        <Route path={"/events"}
                               element={<>
                                   <CreateEvent addTheEvent={eventHook.addTheEvent}
                                                appUser={userHook.appUser}/>
                                   <EventGallery appUser={userHook.appUser}
                                                 theEvents={eventHook.theEvents}/>
                                   <NavigationBar/>
                               </>
                               }/>
                        <Route path={"/events/:eventId"}
                               element={
                                   <TheEventPage theEvents={eventHook.theEvents}
                                                 appUser={userHook.appUser}/>
                               }/>
                        <Route path={"/events/registration/:eventId"} element={
                            <>
                                <AddNewParticipant addParticipant={participantsHook.addParticipant}
                                                   appUser={userHook.appUser}/>
                                <ParticipantsList participants={participantsHook.participants}
                                                  deleteParticipant={participantsHook.deleteParticipant}
                                                  appUser={userHook.appUser}
                                                  getAllParticipants={participantsHook.getAllParticipants}/>
                                <NavigationBarInsideEvent/>
                            </>
                        }/>

                        <Route path={"/events/pairs/:eventId"} element={
                            <>
                                <PairGeneration generatePairs={participantsHook.generatePairs}
                                                appUser={userHook.appUser}
                                                getAllParticipants={participantsHook.getAllParticipants}
                                                getCurrentRound={participantsHook.getCurrentRound}
                                                currentRound={participantsHook.currentRound}/>
                                <NavigationBarInsideEvent/>
                            </>
                        }/>
                        <Route path={"/participants/edit/:eventId/:id"}
                               element={
                                       <ParticipantDetails participants={participantsHook.participants}
                                                           editParticipant={participantsHook.editParticipant}
                                                           appUser={userHook.appUser}
                                                           getAllParticipants={participantsHook.getAllParticipants}/>

                               }/>
                        <Route path={"/events/likes/:eventId"}
                               element={
                                   <>
                                       <LikesCollection sendLike={participantsHook.sendLike}
                                                        participants={participantsHook.participants}
                                                        appUser={userHook.appUser}
                                                        getAllParticipants={participantsHook.getAllParticipants}
                                       />
                                       <NavigationBarInsideEvent/>
                                   </>}/>
                        <Route path={"/events/likes/analysis/:eventId"}
                               element={
                                   <>
                                       <LikesAnalysis
                                           participants={participantsHook.participants}
                                           getAllParticipants={participantsHook.getAllParticipants}
                                           getAllMatches={participantsHook.getAllMatches}
                                           matches={participantsHook.matches}
                                           appUsers={userHook.appUsers}
                                           appUser={userHook.appUser}/>
                                       <NavigationBarInsideEvent/>
                                   </>}/>
                    </Routes>
                </main>
            </HashRouter>
            <ToastContainer className="toast" position="top-center" style={{width: "150px"}}/>
        </>
    );
}
