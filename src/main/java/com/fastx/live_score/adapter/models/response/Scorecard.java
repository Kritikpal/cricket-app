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
public class Scorecard {

    private Long id;

    private Match match;
    private Team team;

    private List<BatsmanStats> batsmanStats;
    private List<BowlerStats> bowlerStats;

    private int totalRuns;
    private int totalWickets;
    private int totalExtras;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}