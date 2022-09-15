import {TheEvent} from "../types/TheEvent";
import {AppUser} from "../types/AppUser";
import {useNavigate, useParams} from "react-router-dom";
import {Button} from "@mui/material";
import "./TheEventPage.css"
import NavigationBarForEvent from "./NavigationBarForEvent";

export default function TheEventPage(props:
                                         {
                                             appUser: AppUser | undefined,
                                             theEvents: TheEvent[],
                                         }) {
    const {eventId} = useParams();
    const thisEvent = props.theEvents.find(theEvent => theEvent.id === eventId);
    const navigate = useNavigate();
    return (<>
            {props.appUser ?
                <>
                    <div className="eventBigName"><h4>{thisEvent?.name} </h4></div>
                    <div id="eventNameAndButtons">
                        <div id="eventButtons">
                            <Button variant="outlined" id="eventPartButton"
                                    onClick={() => {
                                        navigate(`/events/registration/${thisEvent?.id}`)
                                    }}>Reserve a seat
                            </Button>
                            <Button variant="outlined" id="eventPartButton"
                                    onClick={() => {
                                        navigate(`/events/pairs/${thisEvent?.id}`)
                                    }}> Talk
                            </Button>
                            <Button variant="outlined" id="eventPartButton"
                                    onClick={() => {
                                        navigate(`/events/likes/${thisEvent?.id}`)
                                    }}> Send likes
                            </Button>
                            <Button variant="outlined" id="eventPartButton"
                                    onClick={() => {
                                        navigate(`/events/likes/analysis/${thisEvent?.id}`)
                                    }}> Receive matches
                            </Button>
                        </div>
                        <div className="eventName"> {thisEvent?.place} </div>
                        <div className="eventName"> {thisEvent?.time.toLocaleString()} </div>
                        <div className="eventDescription"> {thisEvent?.description} </div>
                    </div>
                    <NavigationBarForEvent/>
                </> : <></>}
        </>
    )
}
