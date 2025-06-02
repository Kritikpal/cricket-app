package com.fastx.live_score.domain.in;

import com.fastx.live_score.adapter.admin.request.PlayerRequest;
import com.fastx.live_score.domain.models.Player;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    void savePlayers(List<PlayerRequest> requests);

    void importPlayersFromCsv(MultipartFile file);

    Player getPlayerById(Long playerId);

    ByteArrayResource exportPlayersToCsv();

    List<Player> listPlayer(String q);

    void updatePlayer(Long playerId, PlayerRequest request);

    void deletePlayer(Long playerId);
}
