package org.fastX.models.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.fastX.enums.Dismissal;
import org.fastX.models.Player;
import org.fastX.models.Score;

@AllArgsConstructor
@Data
@Builder
@Getter
public class BallCompleteEvent implements MatchEvent {
    private final int overNo;
    private final int ballNo;

    private final Dismissal dismissal;
    private final Player bowler;
    private final Player striker;
    private final Player nonStriker;

    private final boolean playersCrossed;
    private final Score runScored;


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
