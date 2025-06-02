package com.fastx.live_score.adapter.admin.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {

    private Long matchId;

    private Long tournamentId;

    @NotNull(message = "Provide Team A id")
    private Long teamAId;

    @NotNull(message = "Provide Team b id")
    private Long teamBId;

    private String venue;
    private int totalOvers;


    private long startTime;
    private long endTime;
}