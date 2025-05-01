package com.fastx.live_score.repository;

import com.fastx.live_score.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {


    @Query("SELECT p FROM PlayerEntity p JOIN p.teams t WHERE t.id = :teamId")
    List<PlayerEntity> findByTeamId(@Param("teamId") Long teamId);

}
