import {FormEvent, useState} from "react";
import Button from '@mui/material/Button';
import SendIcon from '@mui/icons-material/Send';
import './AddNewParticipant.css';

export default function AddNewParticipant(props:
                                              {
                                                  addParticipant: (name: string) => Promise<void>,
                                              }) {

    const [name, setName] = useState("");
    const onNameSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (name === "") return;
        props.addParticipant(name)
        setName("")
    }
    return (
        <form onSubmit={onNameSubmit}>
            <label>What is your name on the nametag? </label>
            <input value={name}
                   onChange={event => setName(event.target.value)}/>
            <Button id="btn" variant="contained" endIcon={<SendIcon/>}>save</Button>
        </form>
    );
}