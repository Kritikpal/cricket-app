package com.fastx.live_score.adapter.ball_user.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;


public record StartMatchRequest(

        @NotNull
        @Size(message = "11 player required", max = 11, min = 11)
        List<Long> teamA,
        @NotNull
        @Size(message = "11 player required", max = 11, min = 11)
        List<Long> teamB) {
}
