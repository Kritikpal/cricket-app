package org.fastX.models.events;

import lombok.Getter;
import org.fastX.models.Player;

@Getter
public class AddBatterInningsEvent implements MatchEvent {
    private final Player player;

    public AddBatterInningsEvent(Player player) {
        this.player = player;
    }
}
