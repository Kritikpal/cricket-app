package com.fastx.live_score.domain.in;

import com.fastx.live_score.domain.Scoreboard;
import org.fastX.models.Match;

import java.io.IOException;

public interface MatchEventService {

    Match startMatch(long matchId) throws IOException, ClassNotFoundException;

    Scoreboard addBallEvent(long matchId, String string);

    Match startOver(long matchId, long bowlerName);

    Scoreboard startInnings(Long matchId, Long teamId, Long strikerId, Long nonStrikerId, Long bowlerId);

    String getScoreBoard(long id);

    Scoreboard getLiveMatchStatus(long id);
}
