package com.fastx.live_score.adapter.ball_user.req;

import jakarta.validation.constraints.NotEmpty;
import org.fastX.enums.DismissType;

public record AddScoreRequest(@NotEmpty() String score,
                              Long dismissBy,
                              DismissType dismissType,
                              Long dismissPlayer) {

}
