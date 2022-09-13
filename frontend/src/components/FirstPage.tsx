import './FirstPage.css';
import {useNavigate} from "react-router-dom";
import {Button} from "@mui/material";

export default function FirstPage() {
    const navigate = useNavigate();

    return (
        <body className="welcome">
        <div id="welcomeMessageAndButtons">
            <h1>
                Welcome to Speed-friending!
            </h1>

            <Button variant="outlined" id="welcomeButton"
                    onClick={() => {
                        navigate("/create-account")
                    }}> Create account
            </Button>

            <Button variant="outlined" id="welcomeButton"
                    onClick={() => {
                        navigate("/login")
                    }}> login
            </Button>
        </div>
        </body>
    )
}