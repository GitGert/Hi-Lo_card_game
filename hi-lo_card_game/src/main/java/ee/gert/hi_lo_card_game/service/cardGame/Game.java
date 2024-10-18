package ee.gert.hi_lo_card_game.service.cardGame;

import ee.gert.hi_lo_card_game.utils.StopWatch;
import lombok.Getter;
import lombok.Setter;
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
    }

    public void addToScore(){
        score +=1;
    }

    public void setPlayerHealth(int playerHealth) {
        if (playerHealth <= 0){
            setGameOver(true);
            stopWatch.stop();
            setGameDuration(Math.round(stopWatch.getElapsedTime()));
        }
        this.playerHealth = playerHealth;
    }

    public void startNewRound(){
        if (deck.getCards().size() <= 1){
            takeNewDeckIntoPlay();
        }
        round = new Round(deck, deck.drawTopCard());
    }

    public void takeNewDeckIntoPlay(){
        deck = new Deck();
        deck.shuffleDeck();
    }
}
