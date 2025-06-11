package com.fastx.live_score.domain.models.match;

import com.fastx.live_score.adapter.admin.response.ListTeamRes;
import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Match {

    private Long id;

    private ListTeamRes teamA;
    private ListTeamRes teamB;

    private int tossWinner;
    private String electedTo;

    private int totalOvers;

    private String venue;
    private MatchStatus matchStatus;

    private int winningTeam;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private long tournament;


}