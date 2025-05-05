package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.fastX.enums.DismissType;

@Data
@Builder
@AllArgsConstructor
public class BallEvent {
    public enum EventType {RUN, WICKET, NO_BALL, WIDE, BYE, LEG_BYE}
    private EventType type;
    private int runs;
    private DismissType disMissalType;
}
