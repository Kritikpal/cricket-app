package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("SELECT m FROM MatchEntity m WHERE m.tournament.id = :tournamentId")
    List<MatchEntity> findMatchesByTournamentId(@Param("tournamentId") Long tournamentId);

}
