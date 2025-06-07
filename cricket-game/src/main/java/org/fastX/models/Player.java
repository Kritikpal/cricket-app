package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Builder
public record Player(Long playerId, String fullName, String shortName) implements Serializable {

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
