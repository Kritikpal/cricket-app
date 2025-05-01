package com.fastx.live_score.adapter.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BallEvent {

    private Long id;

    private Match match;

    private int inningsNumber;
    private int overNumber;
    private int ballNumber;

    private Player batsman;
    private Player nonStriker;
    private Player bowler;

    private int runs;
    private boolean isBoundary;
    private boolean isWicket;
    private String wicketType;

    private Player dismissed;

    private String extraType;
    private int extraRuns;

    private String commentary;

    private LocalDateTime createdAt;


}