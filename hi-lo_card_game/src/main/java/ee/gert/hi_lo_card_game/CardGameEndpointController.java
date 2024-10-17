package ee.gert.hi_lo_card_game;

import ee.gert.hi_lo_card_game.cardGame.Card;
import ee.gert.hi_lo_card_game.cardGame.Deck;
import ee.gert.hi_lo_card_game.cardGame.Game;
import ee.gert.hi_lo_card_game.entity.DbGame;
import ee.gert.hi_lo_card_game.entity.DbUser;
import ee.gert.hi_lo_card_game.gameEnums.Guess;
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

    @GetMapping("/deck")
    public String getProducts() {
        Deck deck = new Deck();
        deck.shuffleDeck();
        return deck.toString();
    }

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;
    //this is the start game that will start all the things.
    // RESPOSE WILL BE:
    // the response: ok and the player card...
    // also should show how many cards are int the stack.


    //TODO: add a response Enum
    //TODO: should let the FE know when the game is reset.
    // I need to get the username from this endpoint
    @GetMapping("/start-round/{username}")
    public Map<String, Object> startRound(@PathVariable("username") String username) {
        System.out.println("got start round request");

        if (game == null || game.isGameOver()) {

            game = new Game();
            game.setUsername(username);
            System.out.println("deck size: " + game.getDeck().getDeckCardCount());
        }
        game.startNewRound();
        //NOTE: I don't like the way username is being set each time. TODO: fix later if have time.
        // the issue is that if the page is refreshed the Be will not start a new session so the BE to send the old session... but session handling is out of scope for this project anyways.
        // and that old session might have a new username so it needs to be set each round.
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


    // this will be the guessing endpoint where I will get the data and check if the request came at the right time
    // possible responses to this request are:
    // CORRECT GUESS  (+1 to score, start new round)
    // TIMED_OUT ()
    // WRONG_GUESS (-1 HEALTH, new round)\
    //TODO: add request enum
    //TODO: add a response Enum
    @PostMapping("/check-guess")
    public Map<String, Object> checkGuess(@RequestBody String userGuess) { // this could be an enum of the 3 guesses.
        System.out.println("got check-guess request with guess:");

        userGuess = userGuess.replace("\"", "").trim();
        System.out.println(userGuess);
        if (game == null || game.isGameOver()) {
            //error: the game should never be null or Game-over when making a guess.
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("type", "error");            ///TODO: make a enum of response types
            errorResponse.put("error", "No game happening right now.");
            errorResponse.put("correct", false);
            return errorResponse; // Spring will automatically convert this to JSON
        }


        boolean correctGuess = game.getRound().checkUserGuess(Guess.valueOf(userGuess));
        //NOTE: I technically don't need to check isRoundexpired here, since checkGuess returns false if expired. but it makes the code more readable so
        if (!correctGuess || game.getRound().isRoundExpired()) {
            //remove health from user
            game.setPlayerHealth(game.getPlayerHealth() - 1);
            if (game.isGameOver()) {
                //send game over response instead.
                Map<String, Object> gameOverResponse = new HashMap<>();
                gameOverResponse.put("type", "game_over");
                gameOverResponse.put("game_over", "You ran out of health");
                gameOverResponse.put("new_health", "0");
                gameOverResponse.put("dealerCard", game.getRound().getDealerCard());
                // Lets try to save the game here:
                System.out.println("trying to save to db... in the wrong place");

                if (userRepository.findByName(game.getUsername()) == null) {
                    DbUser dbUser = new DbUser();
                    dbUser.setName(game.getUsername());
                    userRepository.save(dbUser);
                }

                DbUser dbUserWithId = userRepository.findByName(game.getUsername());

                DbGame dbGame = new DbGame();
                dbGame.setGameDurationInSeconds(game.getGameDuration());
//                dbGame.setDbUser(dbUserWithId);
                dbGame.setCorrectGuessCount(game.getScore());
                gameRepository.save(dbGame);
//                dbUserWithId.getGame().add(dbGame);
                dbUserWithId.addGame(dbGame);
//                dbUserWithId.

                userRepository.save(dbUserWithId);


//                //TODO: this might break
//                game = new Game();

                return gameOverResponse;
            }


            if (game.getRound().isRoundExpired()) {
                Map<String, Object> roundExpiredResponse = new HashMap<>();
                roundExpiredResponse.put("type", "error");
                roundExpiredResponse.put("error", "TIME_OUT");
                roundExpiredResponse.put("new_health", game.getPlayerHealth());
                return roundExpiredResponse;
            }

            //send game over response instead.
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
        // Get all user-s games sorted by:
        // play time
        // score
        DbUser dbUser = userRepository.findByName(username.toLowerCase());
        System.out.println("reading rdbuser.Getgames");
        for (DbGame game : dbUser.getGames()){
            System.out.println("id");
            System.out.println(game.getId());
            System.out.println("duration");
            System.out.println(game.getGameDurationInSeconds());
            System.out.println("count");
            System.out.println(game.getCorrectGuessCount());

        }

        List<DbGame> response ;

        if (sort.equals("correct_guess")){
            response = gameRepository.findByDbUserOrderByGameDurationInSecondsDesc(dbUser);
        }else{
            //default will be by correct guess count
            //FIXME: the find games by user is not working because they are not connected the right way
            response = gameRepository.findByDbUserOrderByCorrectGuessCountDesc(dbUser);
        }
        System.out.println("reading response list:");
        for (DbGame game : response){
            System.out.println("id");
            System.out.println(game.getId());
            System.out.println("duration");
            System.out.println(game.getGameDurationInSeconds());
            System.out.println("count");
            System.out.println(game.getCorrectGuessCount());

        }
        System.out.println(response.toArray().length);


        return gameRepository.findByDbUserOrderByCorrectGuessCountDesc(dbUser);


//        Map<String, Object> response = new HashMap<>();
//        game.addToScore();
//        System.out.println("current score: " + game.getScore());
//        response.put("type", "correct_guess");
//        response.put("correct", correctGuess);
//        response.put("dealerCard", game.getRound().getDealerCard());
//        response.put("new_score", game.getScore());
//        return response;
    }

}
