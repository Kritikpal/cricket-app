package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.BallEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BallEventRepository extends JpaRepository<BallEventEntity, Long> {
}
