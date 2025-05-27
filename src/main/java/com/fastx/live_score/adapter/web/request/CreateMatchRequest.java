package com.fastx.live_score.adapter.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {

    private Long matchId;

    private Long tournamentId;

    @NotNull(message = "Provide Team A id")
    private Long teamAId;

    @NotNull(message = "Provide Team b id")
    private Long teamBId;

    private int tossWinner;
    private String electedTo;
    private int totalOvers;

    private String venue;

    private long startTime;
    private long endTime;
}