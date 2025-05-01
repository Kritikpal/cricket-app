package com.fastx.live_score.adapter.models.response;

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
    private String location;

    private List<ShortMatch> matches;
    private List<Player.ShortTeam> participatingTeams;

    private Player.ShortTeam winner;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AllArgsConstructor
    @Data
    public static class ShortMatch {
        private Long matchId;
        private String teamA;
        private String teamB;
    }
}