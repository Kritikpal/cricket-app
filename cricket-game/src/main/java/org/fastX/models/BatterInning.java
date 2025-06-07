package org.fastX.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.fastX.enums.Dismissal;
import org.fastX.models.events.AddBatterInningsEvent;
import org.fastX.models.events.BallCompleteEvent;

import java.io.Serializable;
import java.util.Objects;

public record BatterInning(Player player, Dismissal dismissal, Balls balls) implements Serializable {

    public static BatterInning createNewStats(AddBatterInningsEvent addBatterInningsEvent) {
        return new BatterInning(addBatterInningsEvent.getPlayer(), null, Balls.newBalls());
    }

    public BatterInning onMatchEvent(BallCompleteEvent event) {
        Balls updatedBalls = this.balls;
        Dismissal updatedDismissal = this.dismissal;

        if (event.striker().equals(player)) {
            updatedBalls = this.balls.add(event);
        }

        if (event.dismissal() != null && event.dismissal().getDismissPlayer().equals(player)) {
            updatedDismissal = event.dismissal();
        }

        return new BatterInning(player, updatedDismissal, updatedBalls);
    }

    @JsonIgnore
    public boolean isOut() {
        return dismissal != null;
    }

    @JsonIgnore
    public boolean isSamePlayer(Player player) {
        return this.player.equals(player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player.playerId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BatterInning other)) return false;
        return Objects.equals(player.playerId(), other.player.playerId());
    }
}
