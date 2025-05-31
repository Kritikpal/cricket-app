package org.fastX.models.events;

import org.fastX.models.Team;

public record MatchStartEvent(Long matchId, Team teamA, Team teamB, int totalOvers,
                              int totalInnings) {
}
