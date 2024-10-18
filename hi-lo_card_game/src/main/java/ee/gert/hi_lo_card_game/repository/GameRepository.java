package ee.gert.hi_lo_card_game.repository;

import ee.gert.hi_lo_card_game.entity.DbGame;
import ee.gert.hi_lo_card_game.entity.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<DbGame, Long> {

    List<DbGame> findByDbUserOrderByCorrectGuessCountDesc(DbUser dbUser);

    List<DbGame> findByDbUserOrderByGameDurationInSecondsDesc(DbUser dbUser);
}
