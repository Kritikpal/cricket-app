package com.fastx.live_score.domain.out;

import org.fastX.models.Match;

public interface MatchCardRepository {

    Match getCachedMatch(long matchId);

    void cacheMatch(Match match);

    void removeLiveMatches(long matchId);
}
