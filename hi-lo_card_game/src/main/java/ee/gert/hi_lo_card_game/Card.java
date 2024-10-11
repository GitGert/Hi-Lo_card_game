package ee.gert.hi_lo_card_game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Card {
    private Suit suit;
    private String rank;
    private int value;

    @Override
    public String toString(){
        return rank  + " of " + suit.toString().toLowerCase()  + " is of value " + value;
    }
}
