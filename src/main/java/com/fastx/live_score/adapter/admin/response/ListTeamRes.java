package com.fastx.live_score.adapter.admin.response;

import com.fastx.live_score.domain.models.match.Team;
import lombok.Getter;

@Getter
public class ListTeamRes {
    private final Long teamId;
    private final String teamName;
    private final String logUrl;

    private ListTeamRes(Long teamId, String teamName, String logUrl) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.logUrl = logUrl;
    }

    public static ListTeamRes from(Team team) {
        return new ListTeamRes(team.getId(),
                team.getName(),
                team.getLogoUrl());
    }
}
