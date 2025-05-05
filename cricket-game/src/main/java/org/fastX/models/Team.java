package org.fastX.models;

import lombok.Data;
import org.fastX.exception.GameException;

import java.util.List;

@Data
public class Team {
    private final Long teamId;
    private final String teamName;
    List<Player> linUp;

    public Team(Long teamId, String teamName, List<Player> linUp) {
        if (linUp == null || linUp.size() != 11) {
            throw new GameException("please add 11 players", 400);
        }
        if (teamId == null) {
            throw new GameException("Add teamId", 400);
        }
        this.teamId = teamId;
        this.teamName = teamName;
        this.linUp = linUp;
    }
}
