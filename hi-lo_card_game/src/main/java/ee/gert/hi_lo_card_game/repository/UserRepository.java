package ee.gert.hi_lo_card_game.repository;

import ee.gert.hi_lo_card_game.entity.DbGame;
import ee.gert.hi_lo_card_game.entity.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {

    DbUser findByName(String name);

    List<DbGame> findByGames_DbUserOrderByGames_CorrectGuessCountAsc(DbUser dbUser);

    List<DbUser> findByNameOrderByGames_CorrectGuessCountAsc(String name);
}
