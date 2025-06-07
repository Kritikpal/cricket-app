package com.fastx.live_score.infra.db;

import com.fastx.live_score.core.utils.SerializationUtils;
import com.fastx.live_score.domain.out.MatchCardRepository;
import com.fastx.live_score.infra.db.entities.LiveMatchCardCacheEntity;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.jpaRepository.LiveMatchCacheJpaRepository;
import com.fastx.live_score.infra.db.jpaRepository.MatchRepository;
import org.fastX.exception.GameException;
import org.fastX.models.Match;
import org.springframework.stereotype.Service;

@Service
public class MatchCardAdaptor implements MatchCardRepository {

    private final LiveMatchCacheJpaRepository liveMatchCacheJpaRepository;
    private final MatchRepository matchRepository;

    public MatchCardAdaptor(LiveMatchCacheJpaRepository liveMatchCacheJpaRepository,
                            MatchRepository matchRepository) {
        this.liveMatchCacheJpaRepository = liveMatchCacheJpaRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public Match getCachedMatch(long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalStateException("Match cache not found"));
        LiveMatchCardCacheEntity cacheEntity = matchEntity.getMatchCardCacheEntity();
        if (cacheEntity == null || cacheEntity.getCache() == null) {
            throw new GameException("Match cache not found please restart the match", 400);
        }

        try {
            return SerializationUtils.deserialize(cacheEntity.getCache(), Match.class);
        } catch (Exception e) {
            throw new GameException("Failed to deserialize match cache", 500);
        }
    }

    @Override
    public void cacheMatch(Match match) {
        MatchEntity matchEntity = matchRepository.findById(match.getMatchId())
                .orElseThrow();
        LiveMatchCardCacheEntity cacheEntity = matchEntity.getMatchCardCacheEntity();
        if (cacheEntity == null) {
            cacheEntity = new LiveMatchCardCacheEntity();
        }

        try {
            // Store serialized bytes directly
            byte[] serialized = SerializationUtils.serialize(match);
            cacheEntity.setCache(serialized);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize match", e);
        }

        matchEntity.setMatchCardCacheEntity(cacheEntity);
        matchRepository.save(matchEntity);
    }

    @Override
    public void removeLiveMatches(long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId)
                .orElseThrow();
        LiveMatchCardCacheEntity cache = matchEntity.getMatchCardCacheEntity();
        if (cache != null) {
            liveMatchCacheJpaRepository.delete(cache);
        }
    }
}
