package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.fastX.enums.MatchStatus;
import org.fastX.exception.GameException;
import org.fastX.models.events.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Match implements MatchEventTrigger<Match> {

    private final Long matchId;
    private final Team teamA;
    private final Team teamB;

    private final int oversPerInnings;
    private final int totalNoOfInnings;

    private MatchStatus matchStatus = MatchStatus.NOT_STARTED;
    private MatchResult matchResult;

    private Innings currentInnings;
    private List<Innings> inningsList = new ArrayList<>();

    public Match(Long matchId, Team teamA, Team teamB, int oversPerInnings, int totalNoOfInnings) {
        if (matchId == null) {
            throw new GameException("Add match Id", 400);
        }
        if (teamA == null || teamB == null) {
            throw new GameException("Add 2 teams", 400);
        }
        if (oversPerInnings < 1) {
            throw new GameException("Add at least 1 over", 400);
        }
        if (totalNoOfInnings < 2) {
            throw new GameException("Add at least 1 innings", 400);
        }
        this.matchId = matchId;
        this.teamA = teamA;
        this.teamB = teamB;
        this.oversPerInnings = oversPerInnings;
        this.totalNoOfInnings = totalNoOfInnings;
    }

    public static Match creaetMatch(MatchStartEvent matchStartEvent) {
        return new Match(matchStartEvent.matchId(),
                matchStartEvent.teamA(),
                matchStartEvent.teamB(),
                matchStartEvent.totalOvers(),
                matchStartEvent.totalInnings())
                .triggerEvent(matchStartEvent);
    }

    @Override
    public Match triggerEvent(MatchEvent event) {
        if (event instanceof MatchStartEvent) {
            this.matchStatus = MatchStatus.ONGOING;
            return this;
        }
        if (!matchStatus.equals(MatchStatus.ONGOING)) {
            throw new GameException("Match is not live", 400);
        }
        if (event instanceof StartInningsEvent startInningsEvent) {
            Innings newScoreCard = Innings.createNewInnings(startInningsEvent);
            this.currentInnings = newScoreCard;
            this.inningsList.add(newScoreCard);
        }
        if (event instanceof BallCompleteEvent) {
            if (this.currentInnings == null) {
                throw new GameException("no scorecard selected", 400);
            }
        }

        this.currentInnings = this.currentInnings.triggerEvent(event);
        return this;
    }

    public Team getBattingTeam() {
        if (currentInnings != null) {
            if (teamA != null && teamB != null) {
                if (currentInnings.getTeam().getTeamId().equals(teamA.getTeamId())) {
                    return teamA;
                } else {
                    return teamB;
                }
            }
        }
        throw new GameException("Select Team", 400);
    }


    public Team getBowlingTeam() {
        if (currentInnings != null) {
            if (teamA != null && teamB != null) {
                if (currentInnings.getTeam().getTeamId().equals(teamA.getTeamId())) {
                    return teamB;
                } else {
                    return teamA;
                }
            }
        }
        throw new GameException("Select Team", 400);
    }

}
