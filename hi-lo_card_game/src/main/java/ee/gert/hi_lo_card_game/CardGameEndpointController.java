package ee.gert.hi_lo_card_game;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController // annotation @ -> sellega votab controller  api requeste vastu

public class CardGameEndpointController {

    @GetMapping("/deck")
    public String getProducts(){
        Deck deck = new Deck();
        return deck.toString();
    }

}
