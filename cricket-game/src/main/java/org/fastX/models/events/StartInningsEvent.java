package org.fastX.models.events;

import org.fastX.models.Player;
import org.fastX.models.Team;

public record StartInningsEvent(
        Team team,
        Player striker,
        Player nonStriker,
        Player bowler
) implements MatchEvent {
}
