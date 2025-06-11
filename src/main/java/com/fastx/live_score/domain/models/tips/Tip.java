package com.fastx.live_score.domain.models.tips;

public record Tip(
        Long tipId,
        String tipData,
        Long matchId
) {
}
