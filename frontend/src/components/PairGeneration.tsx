import {Participant} from "../types/Participant";
import Button from "@mui/material/Button";
import './PairGeneration.css';
import {useState} from "react";


export default function PairGeneration(props: {
    getPairs: () => Promise<void>,
    pairs: Participant[][];
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
        <>
            <Button type="submit" id="getPairsButton" variant="contained" onClick={handleSubmit}>
                Let's get it started! </Button>
            {errorMessage ? <>{errorMessage}</> : <>It's round #{round} now.</>}

            <div>
                {props.pairs.map((array: Participant[]) => {
                    return (
                        <ol>
                            {array.map((participant: Participant) => {
                                return <li>{participant.name}</li>;
                            })}

                        </ol>
                    );
                })}
            </div>
        </>
    );
}