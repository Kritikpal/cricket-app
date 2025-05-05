package org.fastX.models.events;

import lombok.Data;
import org.fastX.models.Player;

@Data
public class OverStartingEvent implements MatchEvent {
    private final Player newBowler;
    private final int overNo;

    public OverStartingEvent(Player newBowler, int overNo) {
        this.newBowler = newBowler;
        this.overNo = overNo;
    }
}
