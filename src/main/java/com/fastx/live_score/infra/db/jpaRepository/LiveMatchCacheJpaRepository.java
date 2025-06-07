package com.fastx.live_score.infra.db.jpaRepository;

import com.fastx.live_score.infra.db.entities.LiveMatchCardCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveMatchCacheJpaRepository extends JpaRepository<LiveMatchCardCacheEntity, Long> {



}
