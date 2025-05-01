package com.fastx.live_score.entities.enums;

import lombok.Getter;

@Getter
public enum MatchStatus {

    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    PAUSED("Paused"),
    CANCELLED("Cancelled");

    private final String statusDescription;

    MatchStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @Override
    public String toString() {
        return statusDescription;
    }
}
