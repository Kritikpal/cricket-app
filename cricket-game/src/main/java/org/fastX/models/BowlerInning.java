package org.fastX.models;

import org.fastX.models.events.BallCompleteEvent;
import org.fastX.models.events.MatchEvent;
import org.fastX.models.events.MatchEventTrigger;

import java.io.Serializable;

public record BowlerInning(Player player, int wicketsTaken,
                           Balls balls) implements MatchEventTrigger<BowlerInning>, Serializable {

    @Override
    public String toString() {
        return "BatterInning{" +
                "player=" + player +
                ", wicketsTaken=" + wicketsTaken +
                ", balls=" + balls +
                '}';
    }

    public static BowlerInning createNewStats(Player player) {
        return new BowlerInning(player, 0, Balls.newBalls());
    }

    @Override
    public BowlerInning triggerEvent(MatchEvent matchEvent) {
        if (matchEvent instanceof BallCompleteEvent ballCompleteEvent &&
                ballCompleteEvent.bowler().equals(this.player)) {

            Balls updatedBalls = this.balls.add(ballCompleteEvent);
            int updatedWickets = this.wicketsTaken;

            if (ballCompleteEvent.dismissal() != null &&
                    ballCompleteEvent.dismissal().getDismissType().isBowler()) {
                updatedWickets++;
            }

            return new BowlerInning(player, updatedWickets, updatedBalls);
        }

        return this;
    }

    public boolean isSamePlayer(Player player) {
        return this.player.playerId().equals(player.playerId());
    }

}
