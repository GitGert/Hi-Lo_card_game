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
  const [timerSeconds, setTimerSeconds] = useState(0) //TODO: implement timer to show to user
  const [timerMinutes, setTimerMinutes] = useState(0)//TODO: implement timer to show to user
  const [score, setScore] = useState(0)
  const [username, setUsername] = useState("")
  const [selectedItem, setSelectedItem] = useState("");
  const [userGames, setUserGames] = useState(null)

  const startRoundApiRequest = async (username ) => {
    setCanGuess(true);
    setPlayerHand("");
    setDealerHand("");
    setGameInfo("");

    try {
      console.log("username: " + username)
      const response = await fetch("http://localhost:8080/start-round/"+username);
      // I should send the user name here as well everyt time that the user does not have a cookie that has their name in there
      
      //check if cookie exists:

        // yes: send the request normally.
      
        // no :
          // set cookie set 

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
          dealerCard = result.dealerCard.rank + " of " + result.dealerCard.suit;
          setInfoTextColor("red")

          setGameInfo("YIKES, THATS WRONG! You ran out of health");
          setHealth(parseInt(result.new_health))
          setDealerHand(dealerCard);
          //TODO: show game over tab with the info about the game, such as:
          // date, timeElapsed
          break
      }



    } catch (err) {
      console.log(err.message);
    }
  };

  const getUserGames = async (username) => {
    try {

      const response = await fetch("http://localhost:8080/games/"+username + "/" + selectedItem); //TODO: change the sort to actual sorting type.


      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const result = await response.json();
      console.log(result)
        setUserGames(result)
    } catch (err) {
      console.log(err.message);
    }
  }



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
      {username && <div style={{display:"flex"}}>{"USERNAME: " + username}</div>}
      {/* <StopwatchAndHealthBar></StopwatchAndHealthBar> */}
      {/* if there is cookie called  */}

      <div id="health">{"HEALTH: " + health}</div>
      <div id="health">{"SCORE:" + score}</div>
      <div id="time">{timerMinutes + ":" + timerSeconds}</div>

      <style>{"body { background-color: black; }"}</style>

      {/* TODO: this looks ugly and is wrong, there should be a differnece between START NEW GAME and NEXT ROUND. */}
      <form style={{ marginBottom: "50px" }}
        onSubmit={(e) => {
          e.preventDefault(); // Prevents default form submission behavior
          
          if (!username){
            const username = e.target.elements.username?.value // Get the value of the input
            setUsername(username)
            startRoundApiRequest(username)
          }else if (username) {
            startRoundApiRequest(username); // Only call the API if username is filled
          } else {
            // Optional: handle the case where the input is empty (alert, focus, etc.)
            console.log("Username is required");
          }
        }}>
        {/* button("START GAME", startRoundApiRequest */}
        {!canGuess && 
        <div>
          {!username && <input placeholder="Username" name="username"  required ></input>}
          <button
          
            // onClick={() => startRoundApiRequest()}
            type="submit"
            >
            {"START GAME"}
          </button>
        </div>
          }
      </form>

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


{username &&    <form 
    style={{margin : "300px"}}
onSubmit={(e)=> {

      e.preventDefault(); // Prevents default form submission behavior
      // e.target.elements.username?.valu
      getUserGames(username)}}>
      <div>
        <label htmlFor="dropdown"></label>
        <select
          id="dropdown"
          value={selectedItem}
          onChange={(e) => setSelectedItem(e.target.value)}
          required
        >
          <option value="">sort by:</option>
          <option value="correct_guess">correct_guess</option>
          <option value="game_duration">game_duration</option>
        </select>
      </div>


      <button type="submit">getUserGames</button>

      {userGames && (
        <div>
          <h3>API Response:</h3>
          <pre>{JSON.stringify(userGames, null, 2)}</pre>
        </div>
      )}

    </form>}
      {/* {button("get user games to console", getUserGames, username)} */}

    </div>
  );
}

export default App;
