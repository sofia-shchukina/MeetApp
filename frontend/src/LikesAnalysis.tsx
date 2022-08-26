import {useState} from "react";
import axios from "axios";
import Button from "@mui/material/Button";
import './LikeAnalysis.css';

export default function LikesAnalysis() {
    const [matches, setMatches] = useState<string[]>([]);

    const getAllMatches = () => {
        axios.get("/participants/likes/analysis")
            .then(response => response.data)
            .then(setMatches)
    }

    return (<form id="receiveMatches">
            <Button type="submit" id="getMatchesButton" variant="contained" onClick={getAllMatches}> Get all
                matches </Button>
            <ul>
                {matches.map((match: string) => <li>{match}</li>)}</ul>
        </form>
    )
}
