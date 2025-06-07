package com.fastx.live_score.infra.db;

import com.fastx.live_score.core.utils.SerializationUtils;
import com.fastx.live_score.domain.out.MatchCardRepository;
import com.fastx.live_score.infra.db.entities.LiveMatchCardCacheEntity;
import com.fastx.live_score.infra.db.entities.MatchEntity;
import com.fastx.live_score.infra.db.jpaRepository.LiveMatchCacheJpaRepository;
import com.fastx.live_score.infra.db.jpaRepository.MatchRepository;
import com.fastx.live_score.infra.file.FileService;
import org.fastX.exception.GameException;
import org.fastX.models.Match;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MatchCardAdaptor implements MatchCardRepository {

    private final LiveMatchCacheJpaRepository liveMatchCacheJpaRepository;
    private final MatchRepository matchRepository;
    private final FileService fileService;

    public MatchCardAdaptor(LiveMatchCacheJpaRepository liveMatchCacheJpaRepository,
                            MatchRepository matchRepository,
                            FileService fileService) {
        this.liveMatchCacheJpaRepository = liveMatchCacheJpaRepository;
        this.matchRepository = matchRepository;
        this.fileService = fileService;
    }

    @Override
    public Match getCachedMatch(long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow(() -> new IllegalStateException("Match cache not found"));
        LiveMatchCardCacheEntity cacheEntity = matchEntity.getMatchCardCacheEntity();
        if (cacheEntity == null) {
            throw new GameException("Match cache not found please restart the match", 400);
        }
        String filePath = cacheEntity.getCache();
        try {
            byte[] fileContent = fileService.readFile(filePath);
            return SerializationUtils.deserialize(fileContent, Match.class);
        } catch (IOException e) {
            throw new GameException("Failed to read cache file", 400);
        }
    }

    @Override
    public void cacheMatch(Match match) {
        MatchEntity matchEntity = matchRepository.findById(match.getMatchId())
                .orElseThrow();
        LiveMatchCardCacheEntity cacheEntity = matchEntity.getMatchCardCacheEntity() == null ? new LiveMatchCardCacheEntity() : matchEntity.getMatchCardCacheEntity();
        byte[] serializedMatch = SerializationUtils.serialize(match);

        try {
            String filePath = fileService.writeMatchCacheFile(match.getMatchId(), serializedMatch);
            cacheEntity.setCache(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write match cache file", e);
        }

        matchEntity.setMatchCardCacheEntity(cacheEntity);
        matchRepository.save(matchEntity);
    }

    @Override
    public void removeLiveMatches(long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).orElseThrow();
        if (matchEntity.getMatchCardCacheEntity() != null) {
            try {
                fileService.deleteFile(matchEntity.getMatchCardCacheEntity().getCache());
            } catch (Exception e) {

            }
            liveMatchCacheJpaRepository.delete(matchEntity.getMatchCardCacheEntity());
        }
    }
}
