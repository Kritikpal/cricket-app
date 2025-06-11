package com.fastx.live_score.domain.in;

import com.fastx.live_score.domain.Scoreboard;
import org.fastX.models.Match;

import java.io.IOException;
import java.util.List;

public interface MatchEventService {

    Match startMatch(long matchId, List<Long> teamA, List<Long> teamB) throws IOException, ClassNotFoundException;

    Scoreboard addBallEvent(long matchId, String string);

    Match startOver(long matchId, long bowlerName);

    Scoreboard startInnings(Long matchId, Long teamId, Long strikerId, Long nonStrikerId, Long bowlerId);

    String getScoreBoard(long id);

    Scoreboard getLiveMatchStatus(long id);
}
