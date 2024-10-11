package ee.gert.hi_lo_card_game;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController // annotation @ -> sellega votab controller  api requeste vastu

public class CardGameEndpointController {
    Game game;

    @GetMapping("/deck")
    public String getProducts(){
        Deck deck = new Deck();
        deck.shuffleDeck();
        return deck.toString();
    }

    //this is the start game that will start all the things.
    @GetMapping("/start-round")
    public String startRound(){
        game = new Game();

        return "";
    }

    // this will be the guessing endpoint where I will get the data and check if the request came at the right time
    // possible responses to this request are:
    // CORRECT GUESS  (+1 to score, start new round)
    // TIMED_OUT ()
    // WRONG_GUESS (-1 HEALTH, new round)
    @PostMapping("/check-guess")
    public String checkGuess(@RequestBody String userGuess){ // this could be an enum of the 3 guesses.

        return "";
    }

    //1. STARTROUND request comes in. In response we will have to:
    //start a 10 second timer
    // BE send FE the card and shows to user.
    //2. client has 10 seconds to make a decision (check if there is a triple handshake possobility for BE to make sure to not start before FE has also recieved start time request)
    // options: HIGHER, LOWER or EQUAL.
    // wait for a USER_GUESS request to arrive. this request has to be tied to the startround 10 second timer (user rooms? with ws?)
    // after 10 seconds has passed send fe a TIME_OUT response... wait how... how do you do that without a ws? - oh, it responds TIME_OUT after the next request.

    // IN BE if TIME_OUT:
    // send removeHealth response.

    //IN FE if "TIME_OUT" or a POINT request or take damadge request recieved move on to next round with the same deck of crads.
    //"TIME_OUT"
    // if time_out start a new turn with the same deck.
    //3. BE request for CHECK GUESS:
    // take the guess and compare it to the top card of the deck, then send either a:
    // givePoint
    //or
    //removeHealth
    //response

    // AFTER (TIMEOUT or GIVEPOINT or REMOVEHEALT) aka can be during cuz no ws.
    // the "dealer" card becomes the player card. and will be remove from deck aka just call takeTopCard from Deck class.


    //4. if after (remove_health) the healht is 0:
    // player is dead send isDead request to FE
    // the score, duration and time should be saved into the database.
    // the prvious deck should be destroyed (a new one can be called for newRound.)

    // 5. if the deck were to run out during the game.
    // pull a new deck into play and shuffle it.


    //implement user with 3 health, []games -> score, date, gameDuration

    // round:
    // 10 second timer starts. --> waiting for response within this time.
    // card is given to user to look at
}
