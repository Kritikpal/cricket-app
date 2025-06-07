package org.fastX.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

public record MatchInfo(Team teamA, Team teamB, int oversPerInnings, int totalNoOfInnings) implements Serializable {
    public static MatchInfo createNewMatch(Team teamA, Team teamB, int oversPerInnings, int totalNoOfInnings) {
        return new MatchInfo(teamA, teamB, oversPerInnings, totalNoOfInnings);
    }
}
