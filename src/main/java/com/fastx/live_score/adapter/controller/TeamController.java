package com.fastx.live_score.adapter.controller;

import com.fastx.live_score.adapter.models.request.CreateTeamRequest;
import com.fastx.live_score.adapter.models.response.Player;
import com.fastx.live_score.adapter.models.response.Team;
import com.fastx.live_score.csv.CsvImporter;
import com.fastx.live_score.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public String importTeams() {
        List<CreateTeamRequest> createTeamRequests = new CsvImporter().readPlayersFromCsv("static/teams.csv", CreateTeamRequest.class);
        teamService.createTeam(createTeamRequests);
        return "Success";
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }


    @PostMapping("/addPlayer/{teamId}")
    public String addPlayerToTeam(@PathVariable Long teamId, @RequestBody List<Long> playerIds) {
        CreateTeamRequest createTeamRequest = new CreateTeamRequest();
        createTeamRequest.setPlayerIds(playerIds);
        teamService.updateTeam(teamId, createTeamRequest);
        return "Success";
    }

}
