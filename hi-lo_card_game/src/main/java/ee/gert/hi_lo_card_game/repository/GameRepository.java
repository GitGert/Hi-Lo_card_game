package ee.gert.hi_lo_card_game.repository;

import ee.gert.hi_lo_card_game.entity.DbGame;
import ee.gert.hi_lo_card_game.entity.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<DbGame, Long> {

    List<DbGame> findByDbUserOrderByCorrectGuessCountDesc(DbUser dbUser);

    List<DbGame> findByDbUserOrderByGameDurationInSecondsDesc(DbUser dbUser);

//
//    // Fetch games by user and sort by game duration (ascending)
//    @Query("SELECT g FROM DbGame g WHERE g.dbUser.id = :userId ORDER BY g.gameDurationInSeconds DESC")
//    List<DbGame> findByUserOrderByGameDuration(@Param("userId") Long userId);
//
//    // Fetch games by user and sort by correct guess count (descending)
//    @Query("SELECT g FROM DbGame g WHERE g.dbUser.id = :userId ORDER BY g.correctGuessCount DESC")
//    List<DbGame> findByUserOrderByCorrectGuessCount(@Param("userId") Long userId);
//    findByDbUserOrderByGameDurationInSecondsDesc
}
