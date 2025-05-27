package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    @Query("SELECT p FROM PlayerEntity p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.shortName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PlayerEntity> searchByName(String query);
}
