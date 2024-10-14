package ee.gert.hi_lo_card_game;

import ee.gert.hi_lo_card_game.cardGame.Card;
import ee.gert.hi_lo_card_game.cardGame.Deck;
import ee.gert.hi_lo_card_game.cardGame.Game;
import ee.gert.hi_lo_card_game.gameEnums.Guess;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    // RESPOSE WILL BE:
    // the response: ok and the player card...
    // also should show how many cards are int the stack.


    //TODO: add a response Enum
    //TODO: should let the FE know when the game is reset.
    @GetMapping("/start-round")
    public  Map<String, Object> startRound(){
        System.out.println("got start round request");

        if (game == null || game.isGameOver()){
            game = new Game();
            System.out.println("deck size: " + game.getDeck().getDeckCardCount());
        }
        game.startNewRound();

        System.out.println("deck size: " + game.getDeck().getDeckCardCount());
        Card playerCard = game.getRound().getPlayerCard();

        Map<String, Object> response = new HashMap<>();

//        response.put("type", "start_round");
        response.put("suit", playerCard.getSuit());
        response.put("rank", playerCard.getRank());
        response.put("value", playerCard.getValue());
        response.put("new_health", game.getPlayerHealth());
        response.put("new_score", game.getScore());

        return response;
    }


    // this will be the guessing endpoint where I will get the data and check if the request came at the right time
    // possible responses to this request are:
    // CORRECT GUESS  (+1 to score, start new round)
    // TIMED_OUT ()
    // WRONG_GUESS (-1 HEALTH, new round)\
    //TODO: add request enum
    //TODO: add a response Enum
    @PostMapping("/check-guess")
    public Map<String, Object> checkGuess(@RequestBody String userGuess){ // this could be an enum of the 3 guesses.
        System.out.println("got check-guess request with guess:");

        userGuess = userGuess.replace("\"", "").trim();
        System.out.println(userGuess);
        if (game == null || game.isGameOver()) {
            //error: the game should never be null or Game-over when making a guess.
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "error");            ///TODO: make a enum of response types
            errorResponse.put("error", "No game happening right now.");
            errorResponse.put("correct", false);
            return errorResponse; // Spring will automatically convert this to JSON
        }



        boolean correctGuess = game.getRound().checkUserGuess(Guess.valueOf(userGuess));
        //NOTE: I technically don't need to check isRoundexpired here, since checkGuess returns false if expired. but it makes the code more readable so
        if (!correctGuess || game.getRound().isRoundExpired()){
            //remove health from user
            game.setPlayerHealth(game.getPlayerHealth()-1);
            if (game.isGameOver()){
                game = new Game();
               //send game over response instead.
                Map<String, Object> gameOverResponse = new HashMap<>();
                gameOverResponse.put("type", "game_over");
                gameOverResponse.put("game_over", "You ran out of health");
                gameOverResponse.put("new_health", "0");
                return gameOverResponse;
            }


            if (game.getRound().isRoundExpired()){
                Map<String, Object> roundExpiredResponse = new HashMap<>();
                roundExpiredResponse.put("type", "error");
                roundExpiredResponse.put("error", "TIME_OUT");
                roundExpiredResponse.put("new_health", game.getPlayerHealth());
                return roundExpiredResponse;
            }

            //send game over response instead.
            Map<String, Object> takeDamageResponse = new HashMap<>();
            takeDamageResponse.put("type", "wrong_guess");
            takeDamageResponse.put("dealerCard", game.getRound().getDealerCard());
            takeDamageResponse.put("player_damage", true);
            takeDamageResponse.put("new_health", game.getPlayerHealth());
            return takeDamageResponse;
        }
        Map<String, Object> response = new HashMap<>();
        game.addToScore();
        System.out.println("current score: " + game.getScore());
        response.put("type", "correct_guess");
        response.put("correct", correctGuess);
        response.put("dealerCard", game.getRound().getDealerCard());
        response.put("new_score", game.getScore());
        return response; // Spring will convert this to JSON
    }

//    {"suit":"DIAMONDS","rank":"7","value":7}
//{"suit":"SPADES","rank":"7","value":7}
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
