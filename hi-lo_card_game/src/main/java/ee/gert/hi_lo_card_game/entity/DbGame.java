package ee.gert.hi_lo_card_game.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne()
    @JsonBackReference
    private DbUser dbUser;
}