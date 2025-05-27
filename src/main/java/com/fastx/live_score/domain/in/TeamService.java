package com.fastx.live_score.domain.in;

import com.fastx.live_score.adapter.web.response.PlayerRes;
import com.fastx.live_score.adapter.web.request.TeamRequest;
import com.fastx.live_score.adapter.web.response.TeamRes;

import java.util.List;

public interface TeamService {
    void saveTeams(List<TeamRequest> request);


    TeamRes getTeamById(Long teamId);

    List<TeamRes> queryTeams(String q);

    void updateTeam(Long teamId, TeamRequest request);

    void addPlayers(Long teamId, List<Long> playerIds);

    void deleteTeam(Long teamId);

    List<PlayerRes> getAllPlayerFromTeam(Long teamId);

    List<TeamRes> getTeamsByTournamentId(Long tournamentId);
}
