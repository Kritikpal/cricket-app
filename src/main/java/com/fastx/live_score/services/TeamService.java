package com.fastx.live_score.services;

import com.fastx.live_score.adapter.models.request.CreateTeamRequest;
import com.fastx.live_score.adapter.models.response.Team;

import java.util.List;

public interface TeamService {
    void createTeam(List<CreateTeamRequest> request);

    Team getTeamById(Long teamId);

    List<Team> getAllTeams();

    void updateTeam(Long teamId, CreateTeamRequest request);

    void deleteTeam(Long teamId);

    List<Team> getTeamsByTournamentId(Long tournamentId);
}
