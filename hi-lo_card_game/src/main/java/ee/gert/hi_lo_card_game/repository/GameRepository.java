package ee.gert.hi_lo_card_game.repository;

import ee.gert.hi_lo_card_game.entity.DbGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<DbGame, Long> {

}
