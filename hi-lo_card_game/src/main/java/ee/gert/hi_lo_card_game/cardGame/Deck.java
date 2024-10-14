package ee.gert.hi_lo_card_game.cardGame;

import ee.gert.hi_lo_card_game.gameEnums.Suit;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Setter
@Getter
public class Deck {
    private List<Card> cards = new ArrayList<>();
    private static final String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
    private static final Suit[] suits = {Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES};

    public Deck(){
        generateDeck();
    }

    public void generateDeck(){
        Pattern pattern = Pattern.compile("\\d+");
        for( String rank : ranks){
            for (Suit suit : suits){
                int value = (pattern.matcher(rank).find() ?  Integer.parseInt(rank) : 10);
                 cards.add( new Card(suit, rank, value));
            }
        }
    }

    public int getDeckCardCount(){
        return cards.size();
    }

    public void shuffleDeck(){
        Collections.shuffle(this.cards);
    }

    public Card drawTopCard(){
        Card topCard = peekAtTopCard();
        cards.remove(0);
        return topCard;
    }

    public Card peekAtTopCard(){
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

    @Override
    public String toString(){
        StringBuilder returnVal = new StringBuilder();
        for (Card card: cards){
            returnVal.append(card.toString()).append("\n");
        }
        return returnVal.toString();
    }
}
