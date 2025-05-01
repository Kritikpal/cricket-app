package com.fastx.live_score.repository;

import com.fastx.live_score.entities.BallEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BallEventRepository extends JpaRepository<BallEventEntity, Long> {
}
