package org.fastX.models;

import lombok.Builder;
import org.fastX.exception.GameException;
import org.fastX.models.events.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
public record Innings(Team team, Balls balls, org.fastX.models.Innings.ScoreCardState scoreCardState,
                      List<BatterInning> batterInnings, List<BowlerInning> bowlerInnings, List<Over> overs,
                      Over currentOver, BatterInning striker, BatterInning nonStriker, BowlerInning currentBowler,
                      BowlerInning lastBowler) implements MatchEventTrigger<Innings>, Serializable {

    public enum ScoreCardState {
        OVER_RUNNING, BETWEEN_OVERS, COMPLETED, WICKET_TAKEN
    }

    public static Innings createNewInnings(StartInningsEvent event) {
        if (event.striker() == null || event.nonStriker() == null || event.bowler() == null
                || event.team() == null) {
            throw new GameException("Invalid Parameters", 400);
        }
        BatterInning striker = BatterInning.createNewStats(new AddBatterInningsEvent(event.striker()));
        List<BatterInning> batterInnings = new ArrayList<>();
        batterInnings.add(striker);


        BatterInning nonStriker = BatterInning.createNewStats(new AddBatterInningsEvent(event.nonStriker()));
        batterInnings.add(nonStriker);

        BowlerInning currentBowler = BowlerInning.createNewStats(event.bowler());

        List<BowlerInning> bowlerInnings = new ArrayList<>();
        bowlerInnings.add(currentBowler);
        Over over = Over.newOver(new OverStartingEvent(currentBowler.player(), 0));

        return Innings.builder()
                .team(event.team())
                .balls(Balls.newBalls())
                .scoreCardState(ScoreCardState.OVER_RUNNING)
                .batterInnings(batterInnings)
                .bowlerInnings(bowlerInnings)
                .overs(List.of(over))
                .currentOver(over)
                .striker(striker)
                .nonStriker(nonStriker)
                .currentBowler(currentBowler)
                .lastBowler(null)
                .build();
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

            Innings updated = withUpdatedBatter(newBatter);
            return updated.toBuilder()
                    .striker(updated.striker == null ? newBatter : updated.striker)
                    .nonStriker(updated.striker != null && updated.nonStriker == null ? newBatter : updated.nonStriker)
                    .build();
        }

        if (event instanceof OverStartingEvent overEvent) {
            Over newOver = Over.newOver(overEvent);

            BowlerInning newBowler = bowlerInnings.stream()
                    .filter(b -> b.player().equals(overEvent.getNewBowler()))
                    .findFirst()
                    .orElse(BowlerInning.createNewStats(overEvent.getNewBowler()));

            Innings updated = this.toBuilder()
                    .overs(new ArrayList<>(this.overs) {{
                        add(newOver);
                    }})
                    .currentOver(newOver)
                    .currentBowler(newBowler)
                    .scoreCardState(ScoreCardState.OVER_RUNNING)
                    .build();

            return updated.withUpdatedBowler(newBowler);
        }

        if (event instanceof BallCompleteEvent ballEvent) {

            Balls newBalls = balls.add(ballEvent);
            Over updatedOver = currentOver.triggerEvent(ballEvent);
            BowlerInning updatedBowler = currentBowler.triggerEvent(ballEvent);
            BatterInning updatedStriker = striker.onMatchEvent(ballEvent);
            BatterInning updatedNonStriker = nonStriker.onMatchEvent(ballEvent);

            Innings updated = this.toBuilder()
                    .balls(newBalls)
                    .currentOver(updatedOver)
                    .currentBowler(updatedBowler)
                    .striker(updatedStriker)
                    .nonStriker(updatedNonStriker)
                    .scoreCardState(nextScoreCardState(ballEvent, updatedOver))
                    .build();

            updated = updated.withUpdatedOver(updatedOver)
                    .withUpdatedBatter(updatedStriker)
                    .withUpdatedBatter(updatedNonStriker)
                    .withUpdatedBowler(updatedBowler);

            if (ballEvent.playersCrossed()) {
                updated = updated.toBuilder()
                        .striker(updated.nonStriker)
                        .nonStriker(updated.striker)
                        .build();
            }

            if (updated.currentOver.isOverCompleted()) {
                updated = updated.toBuilder()
                        .striker(updated.nonStriker)
                        .nonStriker(updated.striker)
                        .build();
            }

            if (ballEvent.dismissal() != null) {
                if (updated.striker.isSamePlayer(ballEvent.dismissal().getDismissPlayer())) {
                    updated = updated.toBuilder().striker(null).build();
                } else {
                    updated = updated.toBuilder().nonStriker(null).build();
                }
            }

            if (updated.batterInnings.stream().filter(BatterInning::isOut).count() >= 10) {
                updated = updated.toBuilder().scoreCardState(ScoreCardState.COMPLETED).build();
            }

            return updated;
        }

        return this;
    }

    private Innings withUpdatedBatter(BatterInning updated) {
        List<BatterInning> updatedList = new ArrayList<>(batterInnings);
        for (int i = 0; i < updatedList.size(); i++) {
            if (updatedList.get(i).isSamePlayer(updated.player())) {
                updatedList.set(i, updated);
                return this.toBuilder().batterInnings(updatedList).build();
            }
        }
        updatedList.add(updated);
        return this.toBuilder().batterInnings(updatedList).build();
    }

    private Innings withUpdatedBowler(BowlerInning updated) {
        List<BowlerInning> updatedList = new ArrayList<>(bowlerInnings);
        for (int i = 0; i < updatedList.size(); i++) {
            if (updatedList.get(i).isSamePlayer(updated.player())) {
                updatedList.set(i, updated);
                return this.toBuilder().bowlerInnings(updatedList).build();
            }
        }
        updatedList.add(updated);
        return this.toBuilder().bowlerInnings(updatedList).build();
    }

    private Innings withUpdatedOver(Over updated) {
        List<Over> updatedOvers = new ArrayList<>(overs);
        for (int i = 0; i < updatedOvers.size(); i++) {
            if (updatedOvers.get(i).equals(updated)) {
                updatedOvers.set(i, updated);
                return this.toBuilder().overs(updatedOvers).build();
            }
        }
        updatedOvers.add(updated);
        return this.toBuilder().overs(updatedOvers).build();
    }

    private ScoreCardState nextScoreCardState(BallCompleteEvent event, Over updatedOver) {
        if (event.dismissal() != null) return ScoreCardState.WICKET_TAKEN;
        if (updatedOver.isOverCompleted()) return ScoreCardState.BETWEEN_OVERS;
        return ScoreCardState.OVER_RUNNING;
    }

}
