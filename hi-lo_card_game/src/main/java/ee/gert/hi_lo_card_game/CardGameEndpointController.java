package ee.gert.hi_lo_card_game;

import ee.gert.hi_lo_card_game.service.cardGame.Card;
import ee.gert.hi_lo_card_game.service.cardGame.Game;
import ee.gert.hi_lo_card_game.entity.DbGame;
import ee.gert.hi_lo_card_game.entity.DbUser;
import ee.gert.hi_lo_card_game.service.cardGame.gameEnums.Guess;
import ee.gert.hi_lo_card_game.repository.GameRepository;
import ee.gert.hi_lo_card_game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController // annotation @ -> sellega votab controller  api requeste vastu

public class CardGameEndpointController {
    Game game;


    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;


    //FIXME: add a response Enum
    //FIXME: should let the FE know when the game is reset.
    @GetMapping("/start-round/{username}")
    public Map<String, Object> startRound(@PathVariable("username") String username) {
        System.out.println("got start round request");

        if (game == null || game.isGameOver()) {

            game = new Game();
            game.setUsername(username);
            System.out.println("deck size: " + game.getDeck().getDeckCardCount());
        }
        game.startNewRound();
        //NOTE: I don't like the way username is being set each time. FIXME: fix later if have time.
        // the issue is that if the page is refreshed the Be will not start a new session so the BE to send the old session... but session handling is out of scope for this project anyways.
        // and that old session might have a different username so it needs to be set each round.
        game.setUsername(username);

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

    //TODO: add request enum
    //TODO: add a response Enum
    @PostMapping("/check-guess")
    public Map<String, Object> checkGuess(@RequestBody String userGuess) { // this could be an enum of the 3 guesses.
        System.out.println("got check-guess request with guess:");

        userGuess = userGuess.replace("\"", "").trim();
        System.out.println(userGuess);
        if (game == null || game.isGameOver()) {
            //FIXME: the game should never be null or Game-over when making a guess.
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "error");            ///TODO: make a enum of response types
            errorResponse.put("error", "No game happening right now.");
            errorResponse.put("correct", false);
            return errorResponse;
        }


        boolean correctGuess = game.getRound().checkUserGuess(Guess.valueOf(userGuess));
        //NOTE: I technically don't need to check isRoundexpired here, since checkGuess returns false if expired. but it makes the code more readable so...
        if (!correctGuess || game.getRound().isRoundExpired()) {
            game.setPlayerHealth(game.getPlayerHealth() - 1);
            if (game.isGameOver()) {
                Map<String, Object> gameOverResponse = new HashMap<>();
                gameOverResponse.put("type", "game_over");
                gameOverResponse.put("game_over", "You ran out of health");
                gameOverResponse.put("new_health", "0");
                gameOverResponse.put("dealerCard", game.getRound().getDealerCard());


                if (userRepository.findByName(game.getUsername()) == null) {
                    DbUser dbUser = new DbUser();
                    dbUser.setName(game.getUsername());
                    userRepository.save(dbUser);
                }

                DbUser dbUserWithId = userRepository.findByName(game.getUsername());

                DbGame dbGame = new DbGame();
                dbGame.setGameDurationInSeconds(game.getGameDuration());
                dbGame.setDbUser(dbUserWithId);
                dbGame.setCorrectGuessCount(game.getScore());
                gameRepository.save(dbGame);


                userRepository.save(dbUserWithId);


                return gameOverResponse;
            }

            if (game.getRound().isRoundExpired()) {
                Map<String, Object> roundExpiredResponse = new HashMap<>();
                roundExpiredResponse.put("type", "error");
                roundExpiredResponse.put("error", "TIME_OUT");
                roundExpiredResponse.put("new_health", game.getPlayerHealth());
                return roundExpiredResponse;
            }

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
        return response;
    }

    @GetMapping("/games/{username}/{sort}")
    public List<DbGame> startRound(@PathVariable("username") String username, @PathVariable("sort") String sort) {
        List<DbGame> response;
        DbUser dbUser = userRepository.findByName(username.toLowerCase());

        if (sort.equals("correct_guess")){
            response = gameRepository.findByDbUserOrderByCorrectGuessCountDesc(dbUser);
        }else{
            //FIXME: currently sorting with game_duration by default so there wouldn't be any errors
            response = gameRepository.findByDbUserOrderByGameDurationInSecondsDesc(dbUser);
        }
        return response;
    }
}
