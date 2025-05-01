package com.fastx.live_score.services;

import com.fastx.live_score.adapter.models.request.AddBallEventRequest;
import com.fastx.live_score.adapter.models.request.CreateMatchRequest;
import com.fastx.live_score.adapter.models.response.BallEvent;
import com.fastx.live_score.adapter.models.response.Match;

import java.util.List;

public interface MatchService {

    Match createMatch(CreateMatchRequest request);

    Match getMatchById(Long matchId);

    List<Match> getMatchesByTournamentId(Long tournamentId);

    BallEvent addBallEvent(AddBallEventRequest request);

    List<BallEvent> getBallEventsByMatchId(Long matchId);

    void startMatch(Long matchId);

    void endMatch(Long matchId);

    String getMatchStatus(Long matchId);

    Long getMatchWinner(Long matchId);
}