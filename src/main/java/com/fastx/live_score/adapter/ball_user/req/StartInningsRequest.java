package com.fastx.live_score.adapter.ball_user.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StartInningsRequest {

    @NotNull
    private Long teamId;

    @NotNull
    private Long striker;

    @NotNull
    private Long nonStriker;

    @NotNull
    private Long bowlerId;
}
