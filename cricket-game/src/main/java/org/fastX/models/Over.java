package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fastX.models.events.BallCompleteEvent;
import org.fastX.models.events.MatchEvent;
import org.fastX.models.events.OverStartingEvent;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Over implements MatchEventTrigger<Over> {

    private final Balls balls;
    private Player bowler;
    private int overNo;
    private int maxBalls = 6;

    private Over(Balls balls, Player bowler, int overNo) {
        this.balls = balls;
        this.bowler = bowler;
        this.overNo = overNo;
    }


    public static Over newOver(OverStartingEvent overStartingEvent) {
        return new Over(Balls.newBalls(), overStartingEvent.getNewBowler(), overStartingEvent.getOverNo());
    }

    public boolean isOverCompleted() {
        return balls.getBalls().size() == 6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(overNo);
    }

    @Override
    public Over triggerEvent(MatchEvent matchEvent) {
        if (matchEvent instanceof BallCompleteEvent ballCompleteEvent) {
            if(ballCompleteEvent.getOverNo()==overNo){
                this.balls.add(ballCompleteEvent);
            }
        }
        return this;
    }
}
