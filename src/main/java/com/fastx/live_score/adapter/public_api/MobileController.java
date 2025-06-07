package com.fastx.live_score.adapter.public_api;

import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.domain.in.MatchService;
import com.fastx.live_score.domain.in.TeamService;
import com.fastx.live_score.domain.in.TournamentService;
import com.fastx.live_score.domain.models.Match;
import com.fastx.live_score.adapter.admin.response.ListTournamentRes;
import com.fastx.live_score.domain.models.Player;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Mobile Controller")
public class MobileController {
    private final MatchService matchService;
    private final TournamentService tournamentService;
    private final TeamService teamService;

    @Autowired
    public MobileController(MatchService matchService, TournamentService tournamentService, TeamService teamService) {
        this.matchService = matchService;
        this.tournamentService = tournamentService;
        this.teamService = teamService;
    }

    @GetMapping("/matchList/{tourId}")
    public AppResponse<List<Match>> loadAllTournamentMatches(@PathVariable Long tourId) {
        return AppResponse.success(matchService.listMatchesByTourId(tourId));
    }


    @GetMapping("/listTournament")
    public AppResponse<List<ListTournamentRes>> getAllTournaments() {
        return AppResponse.success(
                tournamentService.getAllTournaments()
                .stream().map(ListTournamentRes::from).toList());
    }

    @GetMapping("/getPlayerList/{teamId}")
    public AppResponse<List<Player>> getMatchList(@PathVariable Long teamId) {
        return AppResponse.success(teamService.getAllPlayerFromTeam(teamId));
    }


}
