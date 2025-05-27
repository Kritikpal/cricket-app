package com.fastx.live_score.adapter.web.controller;

import com.fastx.live_score.adapter.web.request.TournamentRequest;
import com.fastx.live_score.adapter.web.response.ShortTournamentRes;
import com.fastx.live_score.adapter.web.response.TournamentRes;
import com.fastx.live_score.infra.config.ApiDocsTags;
import com.fastx.live_score.domain.in.TournamentService;
import com.fastx.live_score.application.utils.AppResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.infra.config.APiConfig.API_VERSION_1;

@RestController
@Tag(name = "Tournament", description = "All tournament apis")
@RequestMapping(API_VERSION_1 + "/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/create")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            """
                                    {
                                      "tournamentId":1,
                                      "name": "World Cup",
                                      "description": "Cricket world cup",
                                      "startDate": 1746602788,
                                      "endDate": 1749281188,
                                      "location": "India",
                                      "participatingTeamIds": [
                                        1,2,3,4
                                      ]
                                    }"""
                    )
            )
    )
    public AppResponse<TournamentRes> saveTournament(@RequestBody TournamentRequest request) {
        return AppResponse.success(tournamentService.saveTournament(request));
    }

    @PutMapping("/update/{id}")
    public AppResponse<TournamentRes> updateTournament(@PathVariable Long id, @RequestBody TournamentRequest request) {
        return AppResponse.success(tournamentService.updateTournament(id, request));
    }

    @GetMapping("/getTournamentInfo/{id}")
    public AppResponse<TournamentRes> getTournamentById(@PathVariable Long id) {
        return AppResponse.success(tournamentService.getTournamentById(id));
    }

    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchTournament")
    public AppResponse<List<ShortTournamentRes>> getAllTournaments() {
        return AppResponse.success(tournamentService.getAllTournaments());
    }


    @DeleteMapping("/delete/{id}")
    public AppResponse<String> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return AppResponse.success("Tournament deleted successfully.");
    }

    @PutMapping("/{tournamentId}/assign-winner/{teamId}")
    public AppResponse<String> assignWinner(@PathVariable Long tournamentId, @PathVariable Long teamId) {
        tournamentService.assignWinner(tournamentId, teamId);
        return AppResponse.success("Winner assigned successfully.");
    }
}
