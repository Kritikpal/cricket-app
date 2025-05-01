package com.fastx.live_score.services.servicesImpl;

import com.fastx.live_score.adapter.models.request.AddBallEventRequest;
import com.fastx.live_score.adapter.models.request.CreateMatchRequest;
import com.fastx.live_score.adapter.models.response.BallEvent;
import com.fastx.live_score.adapter.models.response.Match;
import com.fastx.live_score.repository.MatchRepository;
import com.fastx.live_score.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {


    @Override
    public Match createMatch(CreateMatchRequest request) {
        return null;
    }

    @Override
    public Match getMatchById(Long matchId) {
        return null;
    }

    @Override
    public List<Match> getMatchesByTournamentId(Long tournamentId) {
        return List.of();
    }

    @Override
    public BallEvent addBallEvent(AddBallEventRequest request) {
        return null;
    }

    @Override
    public List<BallEvent> getBallEventsByMatchId(Long matchId) {
        return List.of();
    }

    @Override
    public void startMatch(Long matchId) {

    }

    @Override
    public void endMatch(Long matchId) {

    }

    @Override
    public String getMatchStatus(Long matchId) {
        return "";
    }

    @Override
    public Long getMatchWinner(Long matchId) {
        return 0L;
    }
}
