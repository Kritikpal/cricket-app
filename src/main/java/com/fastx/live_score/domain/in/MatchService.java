package com.fastx.live_score.domain.in;

import com.fastx.live_score.adapter.web.request.CreateMatchRequest;
import com.fastx.live_score.adapter.web.response.MatchRes;

public interface MatchService {

    MatchRes saveMatch(CreateMatchRequest request);

    MatchRes getMatchById(Long matchId);

    MatchRes startMatch(Long matchId);

    MatchRes endMatch(Long matchId, int winingTeam);


}