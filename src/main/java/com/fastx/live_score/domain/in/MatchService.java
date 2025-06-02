package com.fastx.live_score.domain.in;

import com.fastx.live_score.adapter.admin.request.MatchRequest;
import com.fastx.live_score.domain.models.Match;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;

import java.util.List;

public interface MatchService {

    void saveMatch(MatchRequest request);

    List<Match> listMatches(MatchStatus matchStatus);

    List<Match> listMatchesByTourId(long tourId);

    Match getMatchById(Long matchId);

    void startMatch(Long matchId);

    void endMatch(Long matchId, int winingTeam);


}