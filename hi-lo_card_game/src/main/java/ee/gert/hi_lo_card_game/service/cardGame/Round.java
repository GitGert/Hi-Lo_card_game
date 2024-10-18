package ee.gert.hi_lo_card_game.service.cardGame;


import ee.gert.hi_lo_card_game.service.cardGame.gameEnums.Guess;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Round {
    int secondsElapsed; // if this is 0 we take no more requests.
    Deck deck;
    Card playerCard;
    Card dealerCard;
    boolean roundExpired;

    public Round(Deck deck, Card playerCard){
        this.deck = deck;
        this.playerCard = playerCard;
        roundExpired = false;
        System.out.println("starting timer:");
        setTimeout(() -> setRoundExpired(true), 10000);
    }

    public Boolean checkUserGuess(Guess userGuess){
        if (roundExpired){
            return  false;
        }
        this.dealerCard = deck.peekAtTopCard();
        Guess correctGuess = getCorrectGuess();
        return  correctGuess == userGuess;
    }

    private Guess getCorrectGuess(){
        if (this.dealerCard.getValue() == this.playerCard.getValue()){
            return Guess.EQUAL;
        }else if (this.dealerCard.getValue() < this.playerCard.getValue()){
            return Guess.LOWER;
        }else{
            return  Guess.HIGHER;
        }
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
