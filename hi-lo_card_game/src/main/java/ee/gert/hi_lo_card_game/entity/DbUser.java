package ee.gert.hi_lo_card_game.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class DbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    private String name;

    @OneToMany()
    @JoinColumn(name="db_user_id")
    @JsonManagedReference
    private List<DbGame> games;
//    private


//    @OneToMany(cascade = CascadeType.ALL) //(cascade = CascadeType.ALL)
//    @JoinColumn(name = "db_user_id", referencedColumnName = "id")
//    private List<DbGame> games = new ArrayList<>();
//@OneToMany(mappedBy = "dbUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Add mappedBy and cascade
//private List<DbGame> games = new ArrayList<>(); // Initialize the list

//    public void addGame(DbGame game) {
//        this.games.add(game);
//    }
}



//7. Make it possible to register player’s name to database and add every finished game
//with the count of correct answers and time of play to the database associated with
//the player.

//8. Make scoreboard with the possibility to sort by duration or correct answers.
//FE

//9. Make it possible to click on the name and show all this player’s games.

//10. Have sufficient unit test coverage
