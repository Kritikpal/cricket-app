package org.fastX.models.events;

import org.fastX.models.Team;

public record StartInningsEvent(
        Team team
) implements MatchEvent {
}
