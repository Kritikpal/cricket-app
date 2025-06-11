package org.fastX;

import lombok.Getter;
import org.fastX.enums.DismissType;
import org.fastX.enums.Dismissal;
import org.fastX.exception.GameException;
import org.fastX.models.*;
import org.fastX.models.events.*;

import java.util.Optional;

@Getter
public class MatchController {
    private Match match;

    public MatchController(Match match) {
        this.match = match;
    }

    public static MatchController startMatch(Long id, Team teamA, Team teamB, int maxOversPerInning, int noOfInnings) {
        MatchStartEvent startEvent = new MatchStartEvent(id, teamA, teamB, maxOversPerInning, noOfInnings);
        return new MatchController(Match.createMatch(startEvent));
    }

    public MatchController startNewInnings(Long teamId, long strikerId, long nonStrikerId, Long bowlerId) {

        Team battingTeam = getTeamById(teamId);
        Team bowlingTeam = getTeamById(teamId);
        if (match.matchInfo().teamA().teamId().equals(battingTeam.teamId())) {
            bowlingTeam = match.matchInfo().teamB();
        }
        match = match.triggerEvent(new StartInningsEvent(
                battingTeam, battingTeam.getPlayerById(strikerId), battingTeam.getPlayerById(nonStrikerId), bowlingTeam.getPlayerById(bowlerId)
        ));
        return this;
    }

    public MatchController startOver(Long bowlerId) {
        Player bowler = getPlayerFromBowlingTeam(bowlerId);
        int overNo = match.currentInnings().overs().size();
        match = match.triggerEvent(new OverStartingEvent(bowler, overNo));
        return this;
    }

    public MatchController addBatter(Long playerId) {
        Player batter = match.getBattingTeam().getPlayerById(playerId);
        match = match.triggerEvent(new AddBatterInningsEvent(batter));
        return this;
    }

    public MatchController completeDelivery(String scoreString) {
        Score score = Score.parse(scoreString);
        BallCompleteEvent event = buildScoreEvent(score).build();
        match = match.triggerEvent(event);
        return this;
    }

    public MatchController completeDelivery(String scoreString, DismissType dismissType, Long dismissedBy, Long dismisPlayer) {
        Score score = Score.parse(scoreString);
        BallCompleteEvent.BallCompleteEventBuilder builder = buildScoreEvent(score);
        if (score.isWicket()) {
            builder.dismissal(buildDismissal(dismissType, dismissedBy, dismisPlayer));
        }
        match = match.triggerEvent(builder.build());
        return this;
    }


    private Dismissal buildDismissal(DismissType type, Long dismissedById, Long dismissPlayerId) {
        Innings innings = match.currentInnings();
        if (type.isBowler()) {
            return new Dismissal(type, innings.currentBowler().player(), innings.striker().player());
        }

        if (dismissedById == null) throw new GameException("Dismissed by player ID is required", 3003);

        Player dismissedBy = match.getBowlingTeam().getPlayerById(dismissedById);
        Player dismisPlayer = match.getBattingTeam().getPlayerById(dismissPlayerId);
        return new Dismissal(type, dismissedBy, dismisPlayer);
    }

    private BallCompleteEvent.BallCompleteEventBuilder buildScoreEvent(Score score) {
        Innings innings = match.currentInnings();
        validateDeliveryState(innings);

        Score currentScore = innings.balls().score();

        return BallCompleteEvent.builder()
                .runScored(score)
                .striker(innings.striker().player())
                .nonStriker(innings.nonStriker().player())
                .bowler(innings.currentBowler().player())
                .playersCrossed(guessIfCrossed(score))
                .overNo(currentScore.overCount())
                .ballNo(currentScore.ballCount() + 1);
    }

    public boolean guessIfCrossed(Score score) {
        if (score.batterRuns() == 0 && score.wides() > 0) {
            return score.wides() % 2 == 0;
        }
        return (score.batterRuns() + score.legByes() + score.byes()) % 2 == 1;
    }

    private void validateDeliveryState(Innings innings) {
        if (innings.scoreCardState() != Innings.ScoreCardState.OVER_RUNNING &&
                innings.scoreCardState() != Innings.ScoreCardState.WICKET_TAKEN) {
            throw new GameException("Invalid state: " + innings.scoreCardState(), 400);
        }

        if (innings.striker() == null || innings.nonStriker() == null ||
                innings.currentBowler() == null || innings.currentOver() == null) {
            throw new GameException("Incomplete setup", 400);
        }

        if (innings.striker().equals(innings.nonStriker())) {
            throw new GameException("Striker and non-striker cannot be the same", 400);
        }
    }

    private Team getTeamById(Long teamId) {
        if (match.matchInfo().teamA().teamId().equals(teamId)) return match.matchInfo().teamA();
        if (match.matchInfo().teamB().teamId().equals(teamId)) return match.matchInfo().teamB();
        throw new GameException("Select valid team", 400);
    }

    private Player getPlayerFromBowlingTeam(Long playerId) {
        return Optional.ofNullable(match.getBowlingTeam())
                .orElseThrow()
                .linUp()
                .stream()
                .filter(p -> p.playerId().equals(playerId))
                .findFirst()
                .orElseThrow();
    }
}
