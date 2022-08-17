import React, {useState} from 'react';
import './App.css';
import axios from "axios";

export default function App() {

  const[message, setMessage] = useState();

  axios.get("/hello")
      .then(response => response.data)
      .then(setMessage)

  return (
      <h1>{message}</h1>
  );
}
