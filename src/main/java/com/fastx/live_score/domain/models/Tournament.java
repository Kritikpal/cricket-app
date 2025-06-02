package com.fastx.live_score.domain.models;

import com.fastx.live_score.adapter.admin.response.ListTeamRes;
import com.fastx.live_score.infra.db.entities.enums.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TournamentStatus tournamentStatus;
    private String location;
    private String logoUrl;

    private List<Match> matches;
    private List<ListTeamRes> participatingTeams;

    private Team winner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}