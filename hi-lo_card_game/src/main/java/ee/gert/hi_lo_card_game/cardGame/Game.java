package ee.gert.hi_lo_card_game.cardGame;

import ee.gert.hi_lo_card_game.utils.StopWatch;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Game {
    Date date;
    int duration;
    int playerHealth;
    Deck deck;
    Round round;
    boolean gameOver;
    StopWatch stopWatch;
    double gameDuration;
    int score;

    public Game(){
        this.date = new Date();
        this.duration = 0;
        this.playerHealth = 3;
        gameOver = false;
        takeNewDeckIntoPlay();
        stopWatch = new StopWatch();
        stopWatch.start();
        this.score = 0;
    }

    public void addToScore(){
        score +=1;
    }

    public void setPlayerHealth(int playerHealth) {
        if (playerHealth <= 0){
            setGameOver(true);
            stopWatch.stop();
            gameDuration = stopWatch.getElapsedTime();
            //also stop the elapsed timer here and set elapsed time;
        }
        this.playerHealth = playerHealth;
    }

//    public boolean isGameOver(){
//        return playerHealth <=0;
//    }

    public void startNewRound(){
        if (deck.getCards().size() <= 1){
            takeNewDeckIntoPlay();
        }
        //TODO: currently the deal card drawing will also destroy the card which is bad since we will need that "dealer card " to be the next "player card"
        round = new Round(deck, deck.drawTopCard());
    }

    public void takeNewDeckIntoPlay(){
        deck = new Deck();
        deck.shuffleDeck();
    }




    //this got a bit crazy right now. because round and Game is not the same thing.


    //INSIDE OF GAME:
        // date, overall duration, playerhealth.

    //INSIDE OF ROUND:
        // 10 seocods elapsed, deck, playercard,



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
}



// additionally if I have time I would like to add a descision for the palyer to either keep playing with the same deck or take a new one
// aka they can count cards and make guesses based on what cards have already been played (maybe alsoe could add multpile decks like casinos do,)