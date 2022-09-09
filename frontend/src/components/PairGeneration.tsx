import {Participant} from "../types/Participant";
import Button from "@mui/material/Button";
import './PairGeneration.css';
import {useState} from "react";
import {AppUser} from "../types/AppUser";


export default function PairGeneration(props: {
    getPairs: () => Promise<void>,
    pairs: Participant[][],
    appUser: AppUser | undefined,
}) {
    const [errorMessage, setErrorMessage] = useState("");
    const [round, setRound] = useState<number>(0);
    const handleSubmit = () => {
        props.getPairs()

            .then(() => setRound(round + 1))
            .catch((error) => {
                setErrorMessage(error.response.data.message)
            })
    }


    return (
        <div id="talk">
            {props.appUser && props.appUser.role === "admin" ?
                <Button type="submit" id="getPairsButton" variant="contained" onClick={handleSubmit}>
                    Let's get it started! </Button> : <></>}
            {errorMessage ? <>{errorMessage}</> : <>It's round #{round} now.</>}

            <div id="pairsList">
                <h3> Pairs for this round </h3>
                {props.pairs.map((array: Participant[], i: number) => {
                    return (
                        <ol key={i}>
                            {array.map((participant: Participant) => {
                                return <li key={participant.id}>{participant.name}</li>;
                            })}

                        </ol>
                    );
                })}
            </div>
        </div>
    );
}