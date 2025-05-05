package org.fastX.enums;

import lombok.Getter;

@Getter
public enum DismissType {
    BOWLED(true, "b"),
    TIMED_OUT(false, "to"),
    CAUGHT(true, "c"),
    HANDLED_THE_BALL(false, "hb"),
    HIT_THE_BALL_TWICE(false, "ht"),
    HIT_WICKET(true, "hw"),
    LEG_BEFORE_WICKET(true, "lbw"),
    OBSTRUCTING_THE_FIELD(false, "of"),
    RUN_OUT(false, "ro"),
    STUMPED(true, "st");

    private final boolean bowler;
    private final String abbreviation;

    DismissType(boolean bowler, String abbreviation) {
        this.bowler = bowler;
        this.abbreviation = abbreviation;
    }


    @Override
    public String toString() {
        return abbreviation;
    }
}
