package com.fastx.live_score.domain.in;

import com.fastx.live_score.domain.models.match.Player;
import com.fastx.live_score.adapter.admin.request.TeamRequest;
import com.fastx.live_score.domain.models.match.Team;

import java.util.List;

public interface TeamService {

    void saveTeams(List<TeamRequest> request);

    Team getTeamById(Long teamId);

    List<Team> listTeams(String q);

    void updateTeam(Long teamId, TeamRequest request);

    void deleteTeam(Long teamId);

    List<Player> getAllPlayerFromTeam(Long teamId);

}
