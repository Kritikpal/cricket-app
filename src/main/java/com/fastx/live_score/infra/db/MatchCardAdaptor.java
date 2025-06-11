package com.fastx.live_score.infra.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastx.live_score.domain.out.MatchCardRepository;
import com.fastx.live_score.infra.db.entities.LiveMatchCardCacheEntity;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.jpaRepository.LiveMatchCacheJpaRepository;
import com.fastx.live_score.infra.db.jpaRepository.MatchEntityRepository;
import org.fastX.enums.MatchStatus;
import org.fastX.exception.GameException;
import org.fastX.models.Match;
import org.springframework.stereotype.Service;

@Service
public class MatchCardAdaptor implements MatchCardRepository {

    private final LiveMatchCacheJpaRepository liveMatchCacheJpaRepository;
    private final MatchEntityRepository matchEntityRepository;
    private final ObjectMapper objectMapper;

    public MatchCardAdaptor(LiveMatchCacheJpaRepository liveMatchCacheJpaRepository,
                            MatchEntityRepository matchEntityRepository) {
        this.liveMatchCacheJpaRepository = liveMatchCacheJpaRepository;
        this.matchEntityRepository = matchEntityRepository;
        this.objectMapper = new ObjectMapper(); // could also be injected as a bean
    }

    @Override
    public Match getCachedMatch(long matchId) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId)
                .orElseThrow(() -> new IllegalStateException("Match cache not found"));
        LiveMatchCardCacheEntity cacheEntity = matchEntity.getMatchCardCacheEntity();
        if (cacheEntity == null || cacheEntity.getCache() == null) {
            throw new GameException("Match cache not found, please restart the match", 400);
        }

        try {
            return objectMapper.readValue(cacheEntity.getCache(), Match.class);
        } catch (Exception e) {
            throw new GameException("Failed to deserialize match cache", 500);
        }
    }

    @Override
    public void cacheMatch(Match match) {
        MatchEntity matchEntity = matchEntityRepository.findById(match.matchId())
                .orElseThrow();
        LiveMatchCardCacheEntity cacheEntity = matchEntity.getMatchCardCacheEntity();
        if (cacheEntity == null) {
            cacheEntity = new LiveMatchCardCacheEntity();
        }
        try {
            String json = objectMapper.writeValueAsString(match);
            cacheEntity.setCache(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize match", e);
        }

        matchEntity.setMatchCardCacheEntity(cacheEntity);
        matchEntityRepository.save(matchEntity);
    }

    @Override
    public void removeLiveMatches(long matchId) {
        MatchEntity matchEntity = matchEntityRepository.findById(matchId)
                .orElseThrow();
        LiveMatchCardCacheEntity cache = matchEntity.getMatchCardCacheEntity();
        if (cache != null) {
            liveMatchCacheJpaRepository.delete(cache);
        }
    }
}
