package com.fastx.live_score.adapter.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PlayerRes {
    private Long id;

    private String fullName;
    private String shortName;
    private String nationality;

    private List<ShortTeamRes> teams;
    private String role;
    private String battingStyle;
    private String bowlingStyle;

    private int totalRuns;
    private int totalWickets;
    private int totalMatches;
    private boolean isActive;


}
