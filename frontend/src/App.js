import logo from "./logo.svg";
import "./App.css";
import React, { useState } from "react";
import StopwatchAndHealthBar from "./stopwatch"

function App() {
  const [canGuess, setCanGuess] = useState(false);
  const [playerHand, setPlayerHand] = useState("");
  const [dealerHand, setDealerHand] = useState("");
  const [gameInfo, setGameInfo] = useState("");
  const [infoTextColor, setInfoTextColor] = useState("white");
  const [health, setHealth] = useState(3)
  const [timerSeconds, setTimerSeconds] = useState(0)
  const [timerMinutes, setTimerMinutes] = useState(0)
  const [score, setScore] = useState(0)
  

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

      setHealth(parseInt(result.new_health))
      let playerCard = result.rank + " of " + result.suit;
      
      setPlayerHand(playerCard);
      setScore(result.new_score)
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
      let dealerCard = ""

      switch(result.type){
        case ("correct_guess"):
          dealerCard = result.dealerCard.rank + " of " + result.dealerCard.suit;

          setScore(parseInt(result.new_score))
          setDealerHand(dealerCard);
          setInfoTextColor("green")
          setGameInfo("YOU WERE RIGHT! Here is a point for you ;)");

          break
        case ("wrong_guess"):
          dealerCard = result.dealerCard.rank + " of " + result.dealerCard.suit;
          setInfoTextColor("red")
          setDealerHand(dealerCard);
          setGameInfo("YIKES, THATS WRONG! You lose a health :(");
          setHealth(parseInt(result.new_health))
          break
        case ("error"):
          console.log(result.error)
          setInfoTextColor("red")
          setGameInfo(result.error + ": unfortunately you have run out of time :(");
          setHealth(parseInt(result.new_health))
          break
        case ("game_over"):
          setGameInfo("YIKES, THATS WRONG! You ran out of health");
          setHealth(parseInt(result.new_health))
          //TODO: show game over tab with the info about the game, such as:
          // date, timeElapsed
          break
      }



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
      {/* <StopwatchAndHealthBar></StopwatchAndHealthBar> */}

      <div id="health">{"HEALTH: " + health}</div>
      <div id="health">{"SCORE:" + score}</div>
      <div id="time">{timerMinutes + ":" + timerSeconds}</div>

      <style>{"body { background-color: black; }"}</style>

      {/* TODO: this looks ugly and is wrong, there should be a differnece between START NEW GAME and NEXT ROUND. */}
      <div style={{ marginBottom: "50px" }}>
        {!canGuess && button("START GAME", startRoundApiRequest)}
      </div>

      <div id="game-info" style={{ marginBottom: "50px", color:infoTextColor }}>
        {gameInfo && JSON.stringify(gameInfo)}
      </div>

      {dealerHand && (
        <div id="dealer-hand">
          {"DEALERS HAND: " + JSON.stringify(dealerHand)}
        </div>
      )}

      {canGuess && (<>
        <div id="guess-buttons">
          {button("LOWER", checkGuessApiRequest, "LOWER")}
          {button("EQUAL", checkGuessApiRequest, "EQUAL")}
          {button("HIGHER", checkGuessApiRequest, "HIGHER")}
        </div>
          <div style={{margin: "25px"}}>You have 10 seconds to make a decision</div>
      </>
      )}

      {playerHand && (
        <div id="player-hand">{"YOUR HAND: " + JSON.stringify(playerHand)}</div>
      )}
    </div>
  );
}

export default App;
