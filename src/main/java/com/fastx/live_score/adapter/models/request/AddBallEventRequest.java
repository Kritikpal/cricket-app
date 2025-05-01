package com.fastx.live_score.adapter.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddBallEventRequest {

    private Long matchId;
    private int inningsNumber;
    private int overNumber;
    private int ballNumber;

    private Long strikerId;
    private Long nonStrikerId;
    private Long bowlerId;

    private int runs;
    private boolean isBoundary;
    private boolean isWicket;
    private String wicketType;

    private Long dismissedPlayerId;

    private String extraType;
    private int extraRuns;

    private String commentary;
}