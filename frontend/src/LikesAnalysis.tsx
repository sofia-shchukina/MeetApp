import Button from "@mui/material/Button";
import './LikeAnalysis.css';
import {Participant} from "./Participant";

export default function LikesAnalysis(props:
                                          {
                                              participants: Participant[],
                                              getAllMatches: (id: string) => void,
                                              matches: string[],
                                              user: string | undefined
                                          }) {


    const analyser = props.participants.find(participant => participant.email === props.user);
    const handleSubmit = () => {
        if (analyser) props.getAllMatches(analyser.id);
    }

    return (<>
            <>Your name on meetup was {analyser ? analyser.name : <>unknown</>}
                . If it's incorrect, please contact the host to receive your matches :-)
            </>
            <form className="likesForm">
                <Button type="submit" id="getMatchesButton" variant="contained" onClick={handleSubmit}> Get all
                    matches </Button>
                <label> Here is a list of your matches, it is not final, it will be updated, as soon as other
                    participants
                    will send their likes. Come here later and press the button again:-) </label>
                <div id="listOfMatches">
                    <h3> List of matches </h3>
                    <ul>
                        {props.matches.length > 0 ? props.matches.map((match: string) => <li>{match}</li>) :
                            <h4>Your don't have any matches yet:-)
                            </h4>}</ul>
                </div>
            </form>
        </>
    )
}
