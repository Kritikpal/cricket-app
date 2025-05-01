package com.fastx.live_score.repository;

import com.fastx.live_score.entities.ScorecardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreCardRepository extends JpaRepository<ScorecardEntity, Long> {
}
