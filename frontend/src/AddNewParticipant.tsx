import {FormEvent, useState} from "react";
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import './AddNewParticipant.css';
import {toast, ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


export default function AddNewParticipant(props:
                                              {
                                                  addParticipant: (name: string) => Promise<void>,
                                              }) {

    const [name, setName] = useState("");
    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (!name) {
            toast.error("Your name should not be empty, try again or contact meetup host")
        } else {
            props.addParticipant(name).then(() => setName("")).catch(() =>
                toast.error("Your name is not saved, please contact meetup host"))
        }
    }
    return (
        <>
            <form onSubmit={onNameSubmit}>
                <label>What is your name on the nametag? </label>
                <input value={name}
                       onChange={event => setName(event.target.value)}/>
                <Button type="submit" id="btn" variant="contained" endIcon={<SendIcon/>}>save</Button>
            </form>
            <ToastContainer className="toast" position="top-center" style={{width: "150px"}}/>
        </>
    );
}