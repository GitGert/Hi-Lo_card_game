package ee.gert.hi_lo_card_game.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
}


//FIXME: make unit tests
//10. Have sufficient unit test coverage
