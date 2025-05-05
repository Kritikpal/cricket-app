package org.fastX.models;

import org.fastX.models.events.MatchEvent;

public interface MatchEventTrigger<T> {
    T triggerEvent(MatchEvent matchEvent);
}
