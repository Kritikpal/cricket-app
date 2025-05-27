package com.fastx.live_score.adapter.web.response;

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
public class TournamentRes {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;

    private List<ShortMatchRes> matches;
    private List<ShortTeamRes> participatingTeams;

    private ShortTeamRes winner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}