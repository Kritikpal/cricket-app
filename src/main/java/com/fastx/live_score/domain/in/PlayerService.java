package com.fastx.live_score.domain.in;

import com.fastx.live_score.adapter.web.response.ShortPlayerRes;
import com.fastx.live_score.adapter.web.request.PlayerRequest;
import com.fastx.live_score.adapter.web.response.PlayerRes;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    void savePlayers(List<PlayerRequest> requests);

    void importPlayersFromCsv(MultipartFile file);

    PlayerRes getPlayerById(Long playerId);

    ByteArrayResource exportPlayersToCsv();

    List<ShortPlayerRes> searchPlayer(String q);

    void updatePlayer(Long playerId, PlayerRequest request);

    void deletePlayer(Long playerId);
}
