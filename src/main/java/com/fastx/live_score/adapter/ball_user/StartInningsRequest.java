package com.fastx.live_score.adapter.ball_user;

import lombok.Data;

@Data
public class StartInningsRequest {
    private Long teamId;
    private Long striker;
    private Long nonStriker;
    private Long bowlerId;
}
