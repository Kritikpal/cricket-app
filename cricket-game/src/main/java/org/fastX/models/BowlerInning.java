package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.fastX.models.events.BallCompleteEvent;
import org.fastX.models.events.MatchEvent;

@AllArgsConstructor
@Data
public class BowlerInning implements MatchEventTrigger<BowlerInning> {

    private final Player player;
    private int wicketsTaken;

    private Balls balls = Balls.newBalls();

    public static BowlerInning createNewStats(Player player) {
        return new BowlerInning(player);
    }

    public BowlerInning(Player player) {
        this.player = player;
    }




    @Override
    public BowlerInning triggerEvent(MatchEvent matchEvent) {
        if (matchEvent instanceof BallCompleteEvent ballCompleteEvent) {
            if (ballCompleteEvent.getBowler().equals(this.player)) {
                balls.add(ballCompleteEvent);
                if (ballCompleteEvent.getDismissal() != null) {
                    if (ballCompleteEvent.getDismissal().getDismissType().isBowler()) {
                        this.wicketsTaken++;
                    }
                }
            }
        }
        return this;
    }
}