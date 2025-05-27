package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    List<TeamEntity> findByNameContainingIgnoreCase(String name);
}
