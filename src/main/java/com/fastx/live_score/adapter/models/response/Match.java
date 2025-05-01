package com.fastx.live_score.adapter.models.response;

import com.fastx.live_score.entities.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Match {

    private Long id;

    private Tournament tournament;

    private Team teamA;
    private Team teamB;

    private int tossWinner;
    private String electedTo;

    private int totalOvers;
    private String venue;
    private MatchStatus matchStatus;

    private int winningTeam;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}