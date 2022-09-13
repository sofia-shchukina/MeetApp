import {Participant} from "../types/Participant";
import Button from "@mui/material/Button";
import './PairGeneration.css';
import './TheEventPage.css';
import {useEffect, useState} from "react";
import {AppUser} from "../types/AppUser";
import {useParams} from "react-router-dom";

export default function PairGeneration(props: {
    generatePairs: (eventId: string) => Promise<void>,
    appUser: AppUser | undefined,
    getAllParticipants: (id: string) => void,
    getCurrentRound: (eventId: string) => void,
    currentRound: Participant[][],
}) {
    const [errorMessage, setErrorMessage] = useState("");
    const {eventId} = useParams();
    useEffect(() => {
        props.getAllParticipants(eventId ? eventId : "fakeId")
        //eslint-disable-next-line
    }, [])

    const handleSubmit = () => {
        props.generatePairs(eventId ? eventId : "fakeID")
            .catch((error) => {
                setErrorMessage(error.response.data.message)
            })
    }
    const handleGetCurrentRoundButton = () => {
        props.getCurrentRound(eventId ? eventId : "fakeID")
    }


    return (<>
            <div className="eventBigName"><h4>Talk</h4></div>

            <div id="talk">
                {props.appUser && props.appUser.role === "admin" ?
                    <Button type="submit" id="getPairsButton" variant="contained" onClick={handleSubmit}>
                        Generate next round </Button> : <></>}
                {errorMessage ? errorMessage : <></>}
                {props.appUser ?
                    <Button type="submit" id="getPairsButton" variant="contained" onClick={handleGetCurrentRoundButton}>
                        See pairs for current round </Button> : <></>}
                <div className="list" id="pairsList">
                    <h3> Pairs for this round </h3>
                    {props.currentRound ? props.currentRound.map((array: Participant[], i: number) => {
                        return (
                            <ol key={i}>
                                {array.map((participant: Participant) => {
                                    return <li key={participant.id}>{participant.name}</li>;
                                })}

                            </ol>
                        );
                }) : <>this round was not generated yet, wait for host to do it, please</>}
            </div>
        </div>
        </>
    );
}