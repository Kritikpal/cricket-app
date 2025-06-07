package org.fastX.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fastX.models.events.BallCompleteEvent;
import org.fastX.models.events.MatchEvent;
import org.fastX.models.events.MatchEventTrigger;
import org.fastX.models.events.OverStartingEvent;

import java.io.Serializable;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class Over implements MatchEventTrigger<Over>  , Serializable {

    private final Balls balls;
    private final Player bowler;
    private final int overNo;
    private final int maxBalls;

    @Override
    public String toString() {
        return "Over{" +
                "balls=" + balls +
                ", bowler=" + bowler +
                ", overNo=" + overNo +
                ", maxBalls=" + maxBalls +
                '}';
    }


    private Over(Balls balls, Player bowler, int overNo) {
        this.balls = balls;
        this.bowler = bowler;
        this.overNo = overNo;
        this.maxBalls = 6;
    }

    public static Over newOver(OverStartingEvent overStartingEvent) {
        return new Over(Balls.newBalls(), overStartingEvent.getNewBowler(), overStartingEvent.getOverNo());
    }

    public boolean isOverCompleted() {
        return balls.getScore().getValidDeliveries() == maxBalls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Over over = (Over) o;
        return overNo == over.overNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(overNo);
    }

    @Override
    public Over triggerEvent(MatchEvent matchEvent) {
        if (matchEvent instanceof BallCompleteEvent ballCompleteEvent &&
                ballCompleteEvent.getOverNo() == this.overNo) {
            Balls updatedBalls = this.balls.add(ballCompleteEvent);
            return new Over(updatedBalls, this.bowler, this.overNo);
        }

        return this;
    }

    @JsonIgnore
    public String getOverString() {

        StringBuilder ballsStr = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for (BallCompleteEvent delivery : this.getBalls()) {
            Score score = delivery.getRunScored();
            ballsStr.append(score.getWording()).append(" ");
        }

        String bowlerName = this.getBowler() != null ? this.getBowler().getFullName() : "Unknown";

        sb.append(String.format("Over %-2d [%s]: %-20s | Runs: %-2d | Wkts: %-2d\n",
                overNo,
                bowlerName,
                ballsStr.toString().trim(),
                this.balls.getScore().totalRunsConceded(),
                this.balls.getScore().getWickets()
        ));
        return sb.toString();
    }


}
