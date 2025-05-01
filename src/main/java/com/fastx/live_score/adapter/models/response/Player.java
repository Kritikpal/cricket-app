package com.fastx.live_score.adapter.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Player {
    private Long id;

    private String fullName;
    private String shortName;
    private String nationality;

    private List<ShortTeam> teams;

    private String role;
    private String battingStyle;
    private String bowlingStyle;

    private int totalRuns;
    private int totalWickets;
    private int totalMatches;
    private boolean isActive;

    @Data
    @AllArgsConstructor
    public static class ShortTeam {
        private Long teamId;
        private String teamName;
    }
}
