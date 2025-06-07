package org.fastX.models;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class MatchInfo implements Serializable {
    private final Team teamA;
    private final Team teamB;
    private final int oversPerInnings;
    private final int totalNoOfInnings;

    private MatchInfo(Team teamA, Team teamB, int oversPerInnings, int totalNoOfInnings) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.oversPerInnings = oversPerInnings;
        this.totalNoOfInnings = totalNoOfInnings;
    }
    public static MatchInfo createNewMatch(Team teamA, Team teamB, int oversPerInnings, int totalNoOfInnings) {
        return new MatchInfo(teamA, teamB, oversPerInnings, totalNoOfInnings);
    }
}
