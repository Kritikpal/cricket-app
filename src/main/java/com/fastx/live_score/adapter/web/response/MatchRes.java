package com.fastx.live_score.adapter.web.response;

import com.fastx.live_score.infra.db.entities.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MatchRes {

    private Long id;

    private TournamentRes tournament;

    private ShortTeamRes teamA;
    private ShortTeamRes teamB;

    private int tossWinner;
    private String electedTo;

    private int totalOvers;
    private String venue;
    private MatchStatus matchStatus;

    private int winningTeam;

    private LocalDateTime startTime;
    private LocalDateTime endTime;


}