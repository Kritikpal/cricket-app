package com.fastx.live_score.adapter.models.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {
    private Long tournamentId;
    private Long teamAId;
    private Long teamBId;
    private int tossWinner;
    private String electedTo;
    private int totalOvers;
    private String venue;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}