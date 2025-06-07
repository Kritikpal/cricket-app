package org.fastX.models.events;

import lombok.Builder;
import org.fastX.enums.Dismissal;
import org.fastX.models.Player;
import org.fastX.models.Score;

import java.io.Serializable;

@Builder
public record BallCompleteEvent(int overNo, int ballNo, Dismissal dismissal, Player bowler, Player striker,
                                Player nonStriker, boolean playersCrossed,
                                Score runScored) implements MatchEvent, Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BallCompleteEvent that)) return false;
        return overNo == that.overNo && ballNo == that.ballNo;
    }

    @Override
    public int hashCode() {
        int result = overNo;
        result = 31 * result + ballNo;
        return result;
    }


}
