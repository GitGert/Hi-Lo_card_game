package ee.gert.hi_lo_card_game;

public class Round {
    int secondsElapsed; // if this is 0 we take no more requests.
    Deck deck;
    Card playerCard;
    Card dealerCard;


    Round(Deck deck, Card playerCard){
        this.deck = deck;
        this.playerCard = playerCard; //TODO: where does this get set?
    }

    public boolean isTimerExpired(){
        return  false;
    }

    public Boolean checkUserGuess(Guess userGuess){
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

    //INSIDE OF ROUND:
    // 10 seocods elapsed, deck, playercard,



}
