import {TheEvent} from "../types/TheEvent";
import {AppUser} from "../types/AppUser";
import {useNavigate, useParams} from "react-router-dom";
import {Button} from "@mui/material";
import "./TheEventPage.css"

export default function TheEventPage(props:
                                         {
                                             appUser: AppUser | undefined,
                                             theEvents: TheEvent[],
                                         }) {
    const {id} = useParams();
    const thisEvent = props.theEvents.find(theEvent => theEvent.id === id);

    const navigate = useNavigate();
    return (<>
            {props.appUser ?

                <div id="eventNameAndButtons">
                    <div className="nameStyle"> {thisEvent?.name} </div>
                    <div className="placeStyle"> {thisEvent?.place} </div>
                    <div className="timeStyle"> {thisEvent?.time} </div>
                    <div className="descriptionStyle"> {thisEvent?.description} </div>
                    <div id="eventButtons">
                        <Button variant="outlined" id="personalButton"
                                onClick={() => {
                                    navigate(`/events/registration/${thisEvent?.id}`)
                                }}>Reserve a seat
                        </Button>
                        <Button variant="outlined" id="personalButton"
                                onClick={() => {
                                    navigate(`/events/pairs/${thisEvent?.id}`)
                                }}> Talk
                        </Button>
                        <Button variant="outlined" id="personalButton"
                                onClick={() => {
                                    navigate(`/events/likes/${thisEvent?.id}`)
                                }}> Send likes
                        </Button>
                        <Button variant="outlined" id="personalButton"
                                onClick={() => {
                                    navigate(`/events/likes/analysis/${thisEvent?.id}`)
                                }}> Receive matches
                        </Button>
                    </div>
                </div>

                : <></>}
        </>
    )
}
