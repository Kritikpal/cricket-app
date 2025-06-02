package com.fastx.live_score.infra.db.entities.enums;

import lombok.Getter;

@Getter
public enum MatchStatus {

    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    PAUSED,
    CANCELLED;
}
