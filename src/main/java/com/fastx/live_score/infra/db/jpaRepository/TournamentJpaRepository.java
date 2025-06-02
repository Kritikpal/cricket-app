package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentJpaRepository extends JpaRepository<TournamentEntity, Long> {
}
