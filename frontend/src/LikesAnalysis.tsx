import {ChangeEvent, useEffect, useState} from "react";
import axios from "axios";
import Button from "@mui/material/Button";
import './LikeAnalysis.css';
import {Participant} from "./Participant";

export default function LikesAnalysis(props:
                                          {
                                              participants: Participant[],
                                          }) {
    const [matches, setMatches] = useState<string[]>([]);
    const [analyserName, setAnalyserName] = useState<string>("");


    const handleAnalyserChange = (event: ChangeEvent<HTMLSelectElement>) => {
        setAnalyserName(event.target.value)
    }
    const handleSubmit = () => {
        const analyser = props.participants.find(participant => participant.name === analyserName);
        if (analyser) getAllMatches(analyser.id);
    }
    const getAllMatches = (id: string) => {
        axios.get("/participants/likes/analysis/" + id)
            .then(response => response.data)
            .then(setMatches)
    }

    useEffect(() => {
        if (props.participants && props.participants.length > 0) {
            setAnalyserName(props.participants[0].name);
        }
    }, [getAllMatches, (props.participants)])

    return (<form className="likesForm">
            <label> What was your name on the Meetup? </label>
            <select value={analyserName} onChange={handleAnalyserChange}>
                {props.participants.map((participant) =>
                    (<option className="option" key={participant.name}
                             value={participant.name}>{participant.name}</option>))}
            </select>
            <Button type="submit" id="getMatchesButton" variant="contained" onClick={handleSubmit}> Get all
                matches </Button>
            <label> Here is a list of your matches, it is not final, it will be updated, as soon as other participants
                will send their likes. Come here later and press the button again:-) </label>
            <div id="listOfMatches">
                <h3> List of matches </h3>
                <ul>
                    {matches.length > 0 ? matches.map((match: string) => <li>{match}</li>) :
                        <h4>Your don't have any matches yet:-)
                        </h4>}</ul>
            </div>
        </form>
    )
}
