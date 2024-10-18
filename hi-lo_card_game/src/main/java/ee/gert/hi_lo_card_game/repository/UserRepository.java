package ee.gert.hi_lo_card_game.repository;


import ee.gert.hi_lo_card_game.entity.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DbUser, Long> {
    DbUser findByName(String name);
}
