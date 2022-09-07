import {Participant} from "../types/Participant";
import Button from "@mui/material/Button";


export default function PairGeneration(props: {
    getPairs: () => void;
    pairs: Participant[][];
}) {

    const handleSubmit = () => {
        props.getPairs();
    }


    return (
        <>
            <Button type="submit" id="getPairsButton" variant="contained" onClick={handleSubmit}>
                Let's get it started! </Button>
            <div>
                {props.pairs.map((array: Participant[], index) => {
                    return (
                        <ol>
                            {array.map((participant: Participant, sIndex) => {
                                return <li>{participant.name}</li>;
                            })}

                        </ol>
                    );
                })}
            </div>
        </>
    );
}