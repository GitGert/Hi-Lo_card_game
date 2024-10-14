import logo from "./logo.svg";
import "./App.css";
import React, { useState } from "react";

function App() {
  const [canGuess, setCanGuess] = useState(false);
  const [playerHand, setPlayerHand] = useState("");
  const [dealerHand, setDealerHand] = useState("");
  const [gameInfo, setGameInfo] = useState("");

  const startRoundApiRequest = async () => {
    setCanGuess(true);
    setPlayerHand("");
    setDealerHand("");
    setGameInfo("");

    try {
      const response = await fetch("http://localhost:8080/start-round");
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const result = await response.json();

      let playerCard = result.rank + " of " + result.suit;

      setPlayerHand(playerCard);
    } catch (err) {
      console.log(err.message);
    }
  };

  const checkGuessApiRequest = async (guess) => {
    setCanGuess(false);
    console.log(guess);
    try {
      const response = await fetch("http://localhost:8080/check-guess", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(guess),
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const result = await response.json();
      if (result.correct) {
        //TODO: add a point
        setGameInfo("YOU WERE RIGHT! Here is a point for you ;)");
      } else if (!result.correct) {
        //TODO: remove a health and display you lose a health message.
        setGameInfo("YIKES, THATS WRONG! You lose a health :(");
      }

      console.log(result);
      let dealerCard = result.dealerCard.rank + " of " + result.dealerCard.suit;

      console.log(dealerCard);
      console.log("aht");

      setDealerHand(dealerCard);
    } catch (err) {
      console.log(err.message);
    }
  };

  function button(buttonName, func, argument = "") {
    return (
      <>
        <div>
          <button
            onClick={() => {
              func(argument);
            }}
          >
            {buttonName}
          </button>
        </div>
      </>
    );
  }

  return (
    <div className="App">
      <style>{"body { background-color: black; }"}</style>

      <div style={{ marginBottom: "50px" }}>
        {!canGuess && button("START GAME", startRoundApiRequest)}
      </div>

      <div id="game-info" style={{ marginBottom: "50px" }}>
        {gameInfo && JSON.stringify(gameInfo)}
      </div>

      {dealerHand && (
        <div id="dealer-Hand">
          {"DEALERS HAND: " + JSON.stringify(dealerHand)}
        </div>
      )}

      {canGuess && (
        <div id="guess-buttons">
          {button("LOWER", checkGuessApiRequest, "LOWER")}
          {button("EQUAL", checkGuessApiRequest, "EQUAL")}
          {button("HIGHER", checkGuessApiRequest, "HIGHER")}
        </div>
      )}

      {playerHand && (
        <div id="player-hand">{"YOUR HAND: " + JSON.stringify(playerHand)}</div>
      )}
    </div>
  );
}

export default App;
