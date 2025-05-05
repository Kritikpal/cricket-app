package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.fastX.exception.GameException;
import org.fastX.models.events.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Innings implements MatchEventTrigger<Innings> {

    public enum ScoreCardState {
        NOT_STARTED, OVER_RUNNING, BETWEEN_OVERS, COMPLETED, WICKET_TAKEN
    }

    private Team team;
    private final Balls balls = Balls.newBalls();
    private ScoreCardState scoreCardState = ScoreCardState.NOT_STARTED;

    private final List<BatterInning> batterInnings = new ArrayList<>();
    private final List<BowlerInning> bowlerInnings = new ArrayList<>();

    private final List<Over> overs = new ArrayList<>();
    private Over currentOver;

    private BatterInning striker;
    private BatterInning nonStriker;
    private BowlerInning currentBowler;
    private BowlerInning lastBowler;

    private Innings(Team team) {
        this.team = team;
    }

    public static Innings createNewInnings(StartInningsEvent inningsEvent) {
        return new Innings(inningsEvent.team());
    }

    private void swapStrike() {
        BatterInning temp = striker;
        striker = nonStriker;
        nonStriker = temp;
    }


    @Override
    public Innings triggerEvent(MatchEvent event) {
        if (scoreCardState == ScoreCardState.COMPLETED) {
            throw new GameException("Scorecard is completed", 400);
        }

        if (event instanceof AddBatterInningsEvent addEvent) {
            BatterInning newBatter = batterInnings.stream()
                    .filter(b -> b.isSamePlayer(addEvent.getPlayer()))
                    .findFirst()
                    .orElse(BatterInning.createNewStats(addEvent));
            updateBatterStat(newBatter);
            if (striker == null) {
                striker = newBatter;
            } else if (nonStriker == null) {
                nonStriker = newBatter;
            }
            if (currentBowler == null) {
                scoreCardState = ScoreCardState.BETWEEN_OVERS;
            }else {
                scoreCardState = ScoreCardState.OVER_RUNNING;
            }
        } else if (event instanceof OverStartingEvent overEvent) {
            BowlerInning newBowlerStats = bowlerInnings.stream()
                    .filter(b -> b.getPlayer().equals(overEvent.getNewBowler()))
                    .findFirst()
                    .orElse(BowlerInning.createNewStats(overEvent.getNewBowler()));
            updateBowlerStat(newBowlerStats);
            scoreCardState = ScoreCardState.OVER_RUNNING;
            currentBowler = newBowlerStats;
            currentOver = overs.stream().filter(o -> o.getOverNo() == overEvent.getOverNo()).findFirst().orElse(
                    Over.newOver(overEvent)
            );
            updateOvers(currentOver);
        } else if (event instanceof BallCompleteEvent ballEvent) {

            ifDeliverPossible();
            balls.add(ballEvent);
            currentOver = currentOver.triggerEvent(ballEvent);
            updateOvers(currentOver);

            BowlerInning updatedBowler = currentBowler.triggerEvent(ballEvent);
            updateBowlerStat(updatedBowler);
            currentBowler = updatedBowler;

            BatterInning strikerStats = getBatsManStat(ballEvent.getStriker()).onMatchEvent(ballEvent);
            BatterInning nonStrikerStats = getBatsManStat(ballEvent.getNonStriker()).onMatchEvent(ballEvent);
            updateBatterStat(strikerStats);
            updateBatterStat(nonStrikerStats);

            striker = strikerStats;
            nonStriker = nonStrikerStats;

            if (ballEvent.isPlayersCrossed()) {
                swapStrike();
            }
            if (currentOver.isOverCompleted()) {
                scoreCardState = ScoreCardState.BETWEEN_OVERS;
                currentOver = null;
                currentBowler = null;
                swapStrike();
            }
            if (ballEvent.getDismissal() != null) {
                scoreCardState = ScoreCardState.WICKET_TAKEN;
                if (striker.isSamePlayer(ballEvent.getDismissal().getDismissPlayer())) {
                    striker = null;
                } else {
                    nonStriker = null;
                }
            }
            if (isAllOut()) {
                scoreCardState = ScoreCardState.COMPLETED;
            }

        }
        return this;
    }


    public void ifDeliverPossible() {
        if (scoreCardState != ScoreCardState.OVER_RUNNING) {
            throw new GameException("Cannot process the ball because state is " + scoreCardState, 400);
        }
        if (striker == null) throw new GameException("Add Striker", 400);
        if (nonStriker == null) throw new GameException("Non Striker", 400);
        if (currentBowler == null) throw new GameException("Current Bowler missing", 400);
        if (currentOver == null) throw new GameException("Current Bowler missing", 400);
        if (striker.equals(nonStriker)) throw new GameException("Striker and non-striker must be different", 400);
    }

    private boolean isAllOut() {
        return batterInnings.stream().filter(BatterInning::isOut).count() >= 10;
    }

    private void updateBatterStat(BatterInning updated) {
        for (int i = 0; i < batterInnings.size(); i++) {
            if (batterInnings.get(i).equals(updated)) {
                batterInnings.set(i, updated);
                return;
            }
        }
        batterInnings.add(updated);
    }

    private void updateBowlerStat(BowlerInning updated) {
        for (int i = 0; i < bowlerInnings.size(); i++) {
            if (bowlerInnings.get(i).getPlayer().equals(updated.getPlayer())) {
                bowlerInnings.set(i, updated);
                return;
            }
        }
        bowlerInnings.add(updated);
    }

    private void updateOvers(Over updated) {
        for (int i = 0; i < overs.size(); i++) {
            if (overs.get(i).equals(updated)) {
                overs.set(i, updated);
                return;
            }
        }
        overs.add(updated);
    }

    BatterInning getBatsManStat(Player target) {
        return batterInnings.stream()
                .filter(b -> b.getPlayer().equals(target))
                .findFirst()
                .orElseThrow(() -> new GameException("Batsman stat not found", 400));
    }
}
