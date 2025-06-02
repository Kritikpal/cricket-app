package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    List<MatchEntity> findByTournament_Id(Long tournamentId);

    List<MatchEntity> findByMatchStatus(MatchStatus matchStatus);
}
