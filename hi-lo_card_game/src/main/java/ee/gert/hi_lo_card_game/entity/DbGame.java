package ee.gert.hi_lo_card_game.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="game")
public class DbGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long correctGuessCount;
    Long gameDurationInSeconds;

    //TODO: for some reason I need to to the joind column with user_id instead of db_user_id... this is weird since that is the actual name of the table ???
    //TODO: figure out why
//    @ManyToOne
//    private DbUser dbUser;
@ManyToOne
@JoinColumn(name = "user_id") // Specify the foreign key column
private DbUser dbUser;
}
//GAME SHOULD HAVE:

//
// count of correct answers
// time of play - aka duration