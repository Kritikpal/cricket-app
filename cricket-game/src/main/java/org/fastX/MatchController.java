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
        if (match.getMatchInfo().getTeamA().getTeamId().equals(battingTeam.getTeamId())) {
            bowlingTeam = match.getMatchInfo().getTeamB();
        }
        match = match.triggerEvent(new StartInningsEvent(
                battingTeam, battingTeam.getPlayerById(strikerId), battingTeam.getPlayerById(nonStrikerId), bowlingTeam.getPlayerById(bowlerId)
        ));
        return this;
    }

    public MatchController startOver(Long bowlerId) {
        Player bowler = getPlayerFromBowlingTeam(bowlerId);
        int overNo = match.getCurrentInnings().getOvers().size();
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

    public MatchController completeDeliveryWithWicket(String scoreString, DismissType dismissType, Long dismissedBy, boolean isStriker) {
        Score score = Score.parse(scoreString);
        BallCompleteEvent.BallCompleteEventBuilder builder = buildScoreEvent(score);

        if (score.isWicket()) {
            builder.dismissal(buildDismissal(dismissType, dismissedBy, isStriker));
        }

        match = match.triggerEvent(builder.build());
        return this;
    }


    private Dismissal buildDismissal(DismissType type, Long dismissedById, boolean isStriker) {
        Innings innings = match.getCurrentInnings();
        if (type.isBowler()) {
            return new Dismissal(type, innings.getCurrentBowler().getPlayer(), innings.getStriker().getPlayer());
        }

        if (dismissedById == null) throw new GameException("Dismissed by player ID is required", 3003);

        Player dismissedBy = match.getBowlingTeam().getPlayerById(dismissedById);
        Player dismissed = isStriker ? innings.getStriker().getPlayer() : innings.getNonStriker().getPlayer();

        return new Dismissal(type, dismissedBy, dismissed);
    }

    private BallCompleteEvent.BallCompleteEventBuilder buildScoreEvent(Score score) {
        Innings innings = match.getCurrentInnings();
        validateDeliveryState(innings);

        Score currentScore = innings.getBalls().getScore();

        return BallCompleteEvent.builder()
                .runScored(score)
                .striker(innings.getStriker().getPlayer())
                .nonStriker(innings.getNonStriker().getPlayer())
                .bowler(innings.getCurrentBowler().getPlayer())
                .playersCrossed(guessIfCrossed(score))
                .overNo(currentScore.overCount())
                .ballNo(currentScore.ballCount() + 1);
    }

    public boolean guessIfCrossed(Score score) {
        if (score.getBatterRuns() == 0 && score.getWides() > 0) {
            return score.getWides() % 2 == 0;
        }
        return (score.getBatterRuns() + score.getLegByes() + score.getByes()) % 2 == 1;
    }

    private void validateDeliveryState(Innings innings) {
        if (innings.getScoreCardState() != Innings.ScoreCardState.OVER_RUNNING &&
                innings.getScoreCardState() != Innings.ScoreCardState.WICKET_TAKEN) {
            throw new GameException("Invalid state: " + innings.getScoreCardState(), 400);
        }

        if (innings.getStriker() == null || innings.getNonStriker() == null ||
                innings.getCurrentBowler() == null || innings.getCurrentOver() == null) {
            throw new GameException("Incomplete setup", 400);
        }

        if (innings.getStriker().equals(innings.getNonStriker())) {
            throw new GameException("Striker and non-striker cannot be the same", 400);
        }
    }

    private Team getTeamById(Long teamId) {
        if (match.getMatchInfo().getTeamA().getTeamId().equals(teamId)) return match.getMatchInfo().getTeamA();
        if (match.getMatchInfo().getTeamB().getTeamId().equals(teamId)) return match.getMatchInfo().getTeamB();
        throw new GameException("Select valid team", 400);
    }

    private Player getPlayerFromBowlingTeam(Long playerId) {
        return Optional.ofNullable(match.getBowlingTeam())
                .orElseThrow()
                .getLinUp()
                .stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow();
    }
}
