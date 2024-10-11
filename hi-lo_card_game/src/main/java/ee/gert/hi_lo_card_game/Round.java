package ee.gert.hi_lo_card_game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Round {
    int secondsElapsed; // if this is 0 we take no more requests.
    Deck deck;
    Card playerCard;
    Card dealerCard;
    Boolean isExpired = false;
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor() ;


    public void setIsExpired(Boolean bool){
        isExpired=bool;
    }
    Runnable task = () -> {
         setIsExpired(true);
    };

    Round(Deck deck, Card playerCard){
        this.deck = deck;
        this.playerCard = playerCard; //TODO: where does this get set?
        //start timer

        //TODO: redo this isExpired part. this does not make sense
        turnOnActuator() ;
        ses.schedule( task , 10 , TimeUnit.SECONDS ) ;
    }

    // one way is to set a timeout with async for 10 second and then modify a ISEXPIRED public boolean.

    public boolean isTimerExpired(){
        //oh wow this is harder that I initially thought.
        return  false;
    }


    public Boolean checkUserGuess(Guess userGuess){

        this.dealerCard = deck.takeTopCard();
        Guess correctGuess = getCorrectGuess();
        //NOTE: I don't know if this works since I am just setting the dealercard and then calling the get CorrectGuess without passing in either of the cards.
        return  correctGuess == userGuess;
    }

    private Guess getCorrectGuess(){
        //TODO: where should I handle the dealerCard...
        // I think I should "reveal" it in Round class in checkUserGuess.
        if (this.dealerCard.getValue() == this.playerCard.getValue()){
            return Guess.EQUAL;
        }else if (this.dealerCard.getValue() < this.playerCard.getValue()){
            return Guess.LOWER;
        }else{
            return  Guess.HIGHER;
        }

    }


    //INSIDE OF ROUND:
    // 10 seocods elapsed, deck, playercard,



}
