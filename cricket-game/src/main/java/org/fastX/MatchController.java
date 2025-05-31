package org.fastX;

import lombok.Getter;
import org.fastX.enums.DismissType;
import org.fastX.enums.Dismissal;
import org.fastX.models.*;
import org.fastX.models.events.*;

@Getter
public class MatchController {
    private Match match;

    public MatchController(Match match) {
        this.match = match;
    }

    public static MatchController startMatch(Long id, Team teamA, Team teamB, int maxOversPerInning, int noOfInnings) {
        return new MatchController(Match.createMatch(new MatchStartEvent(
                id, teamA, teamB, maxOversPerInning, noOfInnings
        )));
    }


    public MatchController startNewInnings(Team team) {
        match = match.triggerEvent(new StartInningsEvent(team));
        return this;
    }


    public MatchController startOver(Long bowlerId) {
        Player bowler = match.getBowlingTeam().getLinUp().stream().filter(player ->
                player.getPlayerId().equals(bowlerId)).findFirst().orElseThrow();
        match = match.triggerEvent(new OverStartingEvent(bowler, match.getCurrentInnings().getOvers().size()));
        return this;
    }


    public MatchController addBatter(Long striker) {
        Player batter = match.getBattingTeam().getLinUp().stream().filter(player ->
                player.getPlayerId().equals(striker)).findFirst().orElseThrow();
        match = match.triggerEvent(new AddBatterInningsEvent(batter));
        return this;
    }

    /**
     * Parses a scorecard value for a score.
     * <p>Examples of scores:</p>
     * <ul>
     *     <li><strong>0</strong> or <strong>.</strong> which returns { #DOT_BALL}</li>
     *     <li><strong>1</strong> which returns { #SINGLE}</li>
     *     <li><strong>2</strong> which returns { #TWO}</li>
     *     <li><strong>3</strong> which returns { #THREE}</li>
     *     <li><strong>4</strong> which returns { #FOUR}</li>
     *     <li><strong>5</strong> which returns a score of 5 runs</li>
     *     <li><strong>6</strong> which returns { #SIX}</li>
     *     <li><strong>1lb</strong> which returns { #LEG_BYE}</li>
     *     <li><strong>2lb</strong> which returns 2 leg byes</li>
     *     <li><strong>W</strong> which returns a wicket without a run</li>
     *     <li><strong>1w</strong> which returns { #WIDE}</li>
     *     <li><strong>1nb</strong> which returns { #NO_BALL}</li>
     *     <li><strong>5nb</strong> which a no-ball with 4 runs off the bat</li>
     *     <li>etc</li>
     * </ul>
     *
     * @param scoreString A score, such as &quot;1&quot;, &quot;1lb&quot; &quot;W&quot; etc
     * @return A built score, or null if unknown
     */
    public MatchController completeDelivery(String scoreString) {
        Score score = Score.parse(scoreString);
        BallCompleteEvent.BallCompleteEventBuilder ballCompleteEvent = buildScoreEvent(score);
        match = match.triggerEvent(ballCompleteEvent.build());
        return this;
    }

    public MatchController completeDelivery(String scoreString, DismissType dismissType) {
        BallCompleteEvent.BallCompleteEventBuilder ballCompleteEventBuilder = buildScoreEvent(Score.parse(scoreString));
        Dismissal dismissal = new Dismissal(dismissType,
                match.getCurrentInnings().getCurrentBowler().getPlayer(),
                dismissType.isBowler() ? match.getCurrentInnings().getStriker().getPlayer() :
                        match.getCurrentInnings().getNonStriker().getPlayer()
        );
        ballCompleteEventBuilder.dismissal(dismissal);
        match = match.triggerEvent(ballCompleteEventBuilder.build());
        return this;
    }

    public MatchController withDismissal(DismissType dismissType, Player player) {
        Dismissal dismissal = new Dismissal(dismissType,
                dismissType.isBowler() ? match.getCurrentInnings().getCurrentBowler().getPlayer() :
                        player,
                dismissType.isBowler() ? match.getCurrentInnings().getStriker().getPlayer() :
                        match.getCurrentInnings().getNonStriker().getPlayer()
        );
        BallCompleteEvent lastEvent = match.getCurrentInnings().getBalls().last();
        BallCompleteEvent ballCompleteEvent = new BallCompleteEvent(lastEvent.getOverNo(),
                lastEvent.getBallNo(),
                dismissal,
                lastEvent.getBowler(),
                lastEvent.getStriker(),
                lastEvent.getNonStriker(),
                lastEvent.isPlayersCrossed(),
                Score.EMPTY
        );
        match = match.triggerEvent(ballCompleteEvent);
        return this;
    }

    private boolean guessIfCrossed(Score score) {
        if (score.getBatterRuns() == 0 && score.getWides() > 0) {
            return score.getWides() % 2 == 0;
        }
        return (score.getBatterRuns() + score.getLegByes() + score.getByes()) % 2 == 1;
    }

    private BallCompleteEvent.BallCompleteEventBuilder buildScoreEvent(Score score) {
        BallCompleteEvent.BallCompleteEventBuilder ballCompleteEvent = BallCompleteEvent.builder();
        ballCompleteEvent.runScored(score);
        ballCompleteEvent.striker(match.getCurrentInnings().getStriker().getPlayer());
        ballCompleteEvent.nonStriker(match.getCurrentInnings().getNonStriker().getPlayer());
        ballCompleteEvent.bowler(match.getCurrentInnings().getCurrentBowler().getPlayer());


        ballCompleteEvent.playersCrossed(guessIfCrossed(score));

        Score currentScore = match.getCurrentInnings().getBalls().getScore();

        ballCompleteEvent.overNo(currentScore.overCount());
        ballCompleteEvent.ballNo(currentScore.ballCount() + 1);
        return ballCompleteEvent;
    }


}
