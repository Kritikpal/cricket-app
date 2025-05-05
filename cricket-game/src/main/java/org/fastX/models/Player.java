package org.fastX.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
public class Player {

    private Long playerId;
    private String fullName;
    private String shortName;

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
