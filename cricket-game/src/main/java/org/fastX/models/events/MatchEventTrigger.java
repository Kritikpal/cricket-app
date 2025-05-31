package org.fastX.models.events;

public interface MatchEventTrigger<T> {
    T triggerEvent(MatchEvent matchEvent);
}
