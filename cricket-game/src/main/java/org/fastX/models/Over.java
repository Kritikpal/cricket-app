package org.fastX.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.fastX.models.events.BallCompleteEvent;
import org.fastX.models.events.MatchEvent;
import org.fastX.models.events.MatchEventTrigger;
import org.fastX.models.events.OverStartingEvent;

import java.io.Serializable;
import java.util.Objects;

public record Over(
        Balls balls,
        Player bowler,
        int overNo,
        int maxBalls
) implements MatchEventTrigger<Over>, Serializable {

    public Over {
        Objects.requireNonNull(balls, "balls must not be null");
        Objects.requireNonNull(bowler, "bowler must not be null");
    }

    public static Over newOver(OverStartingEvent event) {
        return new Over(Balls.newBalls(), event.getNewBowler(), event.getOverNo(), 6);
    }

    @JsonIgnore
    public boolean isOverCompleted() {
        return balls.score().validDeliveries() == maxBalls;
    }

    @Override
    public Over triggerEvent(MatchEvent matchEvent) {
        if (matchEvent instanceof BallCompleteEvent ballCompleteEvent &&
                ballCompleteEvent.overNo() == this.overNo) {
            Balls updatedBalls = this.balls.add(ballCompleteEvent);
            return new Over(updatedBalls, this.bowler, this.overNo, this.maxBalls);
        }
        return this;
    }

    @JsonIgnore
    public String getOverString() {
        StringBuilder ballsStr = new StringBuilder();
        for (BallCompleteEvent delivery : this.balls()) {
            ballsStr.append(delivery.runScored().getWording()).append(" ");
        }

        String bowlerName = this.bowler() != null ? this.bowler().fullName() : "Unknown";

        return String.format(
                "Over %-2d [%s]: %-20s | Runs: %-2d | Wkts: %-2d\n",
                overNo,
                bowlerName,
                ballsStr.toString().trim(),
                this.balls.score().totalRunsConceded(),
                this.balls.score().wickets()
        );
    }

    @Override
    public String toString() {
        return "Over{" +
                "balls=" + balls +
                ", bowler=" + bowler +
                ", overNo=" + overNo +
                ", maxBalls=" + maxBalls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Over over && this.overNo == over.overNo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(overNo);
    }
}
