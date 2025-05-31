package com.fastx.live_score.adapter.web.controller;

import com.fastx.live_score.adapter.web.response.PlayerRes;
import com.fastx.live_score.infra.config.ApiDocsTags;
import com.fastx.live_score.adapter.web.request.TeamRequest;
import com.fastx.live_score.adapter.web.response.TeamRes;
import com.fastx.live_score.domain.in.TeamService;
import com.fastx.live_score.application.utils.AppResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.infra.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/teams")
@Tag(name = "teams", description = "Api for teams")
public class TeamController {

    private final TeamService teamService;


    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }



    @PostMapping("/create")
    public String createTeam(@RequestBody TeamRequest team) {
        teamService.saveTeams(List.of(team));
        return "Teams created successfully.";
    }


    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchTeams")
    public AppResponse<List<TeamRes>> searchTeams(@RequestParam(value = "q", required = false) String q) {
        return AppResponse.success(teamService.listTeams(q));
    }

    @GetMapping("/getTeamInfo/{id}")
    public AppResponse<TeamRes> getTeamById(@PathVariable Long id) {
        return AppResponse.success(teamService.getTeamById(id));
    }

    @PutMapping("/updateTeam/{id}")
    public AppResponse<String> updateTeam(@PathVariable Long id, @RequestBody TeamRequest teamRequest) {
        teamService.updateTeam(id, teamRequest);
        return AppResponse.success("Team updated");
    }

    @PutMapping("/addPlayers/{teamId}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    value = "[1,2,3,4,5,6,7,8,9,10]"
                            )
                    }
            )
    )
    public AppResponse<String> addPlayerToTeam(@PathVariable Long teamId, @RequestBody List<Long> playerIds) {
        teamService.addPlayers(teamId, playerIds);
        return AppResponse.success(null, "Players added successfully.");
    }

    @GetMapping("/getPlayersFromTeam/{teamId}")
    public AppResponse<List<PlayerRes>> getPlayersByTeamId(@PathVariable Long teamId) {
        return AppResponse.success(teamService.getAllPlayerFromTeam(teamId));
    }

    @DeleteMapping("/{id}")
    public AppResponse<String> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return AppResponse.success(null, "Team deleted successfully.");
    }
}
