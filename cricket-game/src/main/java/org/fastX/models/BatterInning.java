package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fastX.enums.Dismissal;
import org.fastX.models.events.AddBatterInningsEvent;
import org.fastX.models.events.BallCompleteEvent;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class BatterInning {

    private final Player player;

    private Dismissal dismissal;

    private Balls balls = Balls.newBalls();

    public BatterInning(Player player) {
        this.player = player;
    }


    public static BatterInning createNewStats(AddBatterInningsEvent addBatterInningsEvent) {
        return new BatterInning(addBatterInningsEvent.getPlayer());
    }


    public BatterInning onMatchEvent(BallCompleteEvent ballCompleteEvent) {
        if (ballCompleteEvent.getStriker().equals(player)) {
            this.balls.add(ballCompleteEvent);
        }
        if (ballCompleteEvent.getDismissal() != null) {
            Dismissal dismissal = ballCompleteEvent.getDismissal();
            if (dismissal.getDismissPlayer().equals(player)) {
                this.dismissal = dismissal;
            }
        }
        return this;
    }


    public boolean isOut() {
        return dismissal != null;
    }


    public boolean isSamePlayer(Player player) {
        return player.equals(this.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player.getPlayerId());
    }


}