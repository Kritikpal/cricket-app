package com.fastx.live_score.adapter.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShortMatchRes {
    private Long matchId;
    private String teamA;
    private String teamB;
}
