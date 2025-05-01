package com.fastx.live_score.repository;

import com.fastx.live_score.entities.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {
}
