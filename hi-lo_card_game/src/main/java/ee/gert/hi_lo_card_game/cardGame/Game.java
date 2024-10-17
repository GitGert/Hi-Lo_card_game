package ee.gert.hi_lo_card_game.cardGame;

import ee.gert.hi_lo_card_game.entity.DbGame;
import ee.gert.hi_lo_card_game.entity.DbUser;
import ee.gert.hi_lo_card_game.repository.GameRepository;
import ee.gert.hi_lo_card_game.repository.UserRepository;
import ee.gert.hi_lo_card_game.utils.StopWatch;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@Service
public class Game {
    Date date;
    int duration;
    int playerHealth;
    Deck deck;
    Round round;
    boolean gameOver;
    StopWatch stopWatch;
    Long gameDuration;
    Long score;
    String username;

    public void setUsername(String username){
        this.username = username.toLowerCase();
    }

    public Game(){
        this.date = new Date();
        this.duration = 0;
        this.playerHealth = 3;
        gameOver = false;
        takeNewDeckIntoPlay();
        stopWatch = new StopWatch();
        stopWatch.start();
        this.score = 0L;

        //make a new Db user here with the username given from FE
        // first check if the user already exists so I wouldn't run into user already exists.
    }

    public void addToScore(){
        score +=1;
    }

//    @Autowired
//    GameRepository gameRepository;
//
//    @Autowired
//    UserRepository userRepository;


    public void setPlayerHealth(int playerHealth) {
        if (playerHealth <= 0){
            setGameOver(true);
            stopWatch.stop();
            setGameDuration(Math.round(stopWatch.getElapsedTime()));
            // make new user if not exist.
//            DbUser dbUser = new DbUser();
//            dbUser.setName(this.username);
//            userRepository.save(dbUser);
//
//            DbUser dbUserWithId = userRepository.findByName(this.username);
//
//
//            DbGame dbGame = new DbGame();
//            dbGame.setGameDurationInSeconds(gameDuration);
//            dbGame.setUser(dbUserWithId);
//            dbGame.setCorrectGuessCount(this.score);
//
//            gameRepository.save(dbGame);

            // make new game entry connected to the current user.
            // save both.

            // make a new dbGame entry here and save it. (user will be connected before already.)
            //add the db stuff here.
            // create a new game here with

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