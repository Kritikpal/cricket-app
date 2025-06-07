package org.fastX.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.fastX.exception.GameException;

import java.io.Serializable;
import java.util.List;

public record Team(Long teamId, String teamName, List<Player> linUp) implements Serializable {
    public Team {
        if (linUp == null || linUp.size() != 11) {
            throw new GameException("please add 11 players", 400);
        }
        if (teamId == null) {
            throw new GameException("Add teamId", 400);
        }
    }

    @JsonIgnore
    public Player getPlayerById(Long id) {
        return linUp.stream().filter(player -> player.playerId().equals(id)).findFirst().orElseThrow();
    }

}
