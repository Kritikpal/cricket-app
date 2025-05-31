package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fastX.models.events.BallCompleteEvent;
import org.fastX.models.events.MatchEvent;
import org.fastX.models.events.MatchEventTrigger;

@Getter
@AllArgsConstructor
public class BowlerInning implements MatchEventTrigger<BowlerInning> {

    private final Player player;
    private final int wicketsTaken;
    private final Balls balls;

    public static BowlerInning createNewStats(Player player) {
        return new BowlerInning(player, 0, Balls.newBalls());
    }

    @Override
    public BowlerInning triggerEvent(MatchEvent matchEvent) {
        if (matchEvent instanceof BallCompleteEvent ballCompleteEvent &&
                ballCompleteEvent.getBowler().equals(this.player)) {

            Balls updatedBalls = this.balls.add(ballCompleteEvent);
            int updatedWickets = this.wicketsTaken;

            if (ballCompleteEvent.getDismissal() != null &&
                    ballCompleteEvent.getDismissal().getDismissType().isBowler()) {
                updatedWickets++;
            }

            return new BowlerInning(player, updatedWickets, updatedBalls);
        }

        return this;
    }
}
