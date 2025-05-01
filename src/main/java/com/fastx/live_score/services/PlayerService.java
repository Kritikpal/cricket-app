package com.fastx.live_score.services;

import com.fastx.live_score.adapter.models.request.CreatePlayerRequest;
import com.fastx.live_score.adapter.models.response.Player;

import java.util.List;

public interface PlayerService {

    void createPlayer(List<CreatePlayerRequest> requests);

    Player getPlayerById(Long playerId);

    List<Player> getPlayersByTeamId(Long teamId);

    List<Player> getAllPlayers();

    Player updatePlayer(Long playerId, CreatePlayerRequest request);

    void deletePlayer(Long playerId);
}
