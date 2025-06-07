package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fastX.enums.Dismissal;
import org.fastX.models.events.AddBatterInningsEvent;
import org.fastX.models.events.BallCompleteEvent;

import java.io.Serializable;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class BatterInning implements Serializable {

    private final Player player;
    private final Dismissal dismissal;
    private final Balls balls;

    @Override
    public String toString() {
        return "BatterInning{" +
                "player=" + player +
                ", dismissal=" + dismissal +
                ", balls=" + balls +
                '}';
    }


    public BatterInning(Player player) {
        this(player, null, Balls.newBalls());
    }

    public static BatterInning createNewStats(AddBatterInningsEvent addBatterInningsEvent) {
        return new BatterInning(addBatterInningsEvent.getPlayer());
    }

    public BatterInning onMatchEvent(BallCompleteEvent event) {
        Balls updatedBalls = this.balls;
        Dismissal updatedDismissal = this.dismissal;

        if (event.getStriker().equals(player)) {
            updatedBalls = this.balls.add(event);
        }

        if (event.getDismissal() != null && event.getDismissal().getDismissPlayer().equals(player)) {
            updatedDismissal = event.getDismissal();
        }

        return new BatterInning(player, updatedDismissal, updatedBalls);
    }

    public boolean isOut() {
        return dismissal != null;
    }

    public boolean isSamePlayer(Player player) {
        return this.player.equals(player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player.getPlayerId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BatterInning other)) return false;
        return Objects.equals(player.getPlayerId(), other.player.getPlayerId());
    }
}
