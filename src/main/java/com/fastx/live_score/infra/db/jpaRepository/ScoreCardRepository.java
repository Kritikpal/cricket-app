package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.ScorecardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreCardRepository extends JpaRepository<ScorecardEntity, Long> {
}
