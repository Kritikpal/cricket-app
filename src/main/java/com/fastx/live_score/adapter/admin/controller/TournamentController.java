package com.fastx.live_score.adapter.admin.controller;

import com.fastx.live_score.adapter.admin.request.TournamentRequest;
import com.fastx.live_score.adapter.admin.response.ListTournamentRes;
import com.fastx.live_score.domain.models.Tournament;
import com.fastx.live_score.core.config.ApiDocsTags;
import com.fastx.live_score.domain.in.TournamentService;
import com.fastx.live_score.core.utils.AppResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

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
    public AppResponse<Tournament> createTournament(@RequestBody TournamentRequest request) {
        return AppResponse.success(tournamentService.createNewTournament(request));
    }

    @PutMapping("/update/{id}")
    public AppResponse<Tournament> updateTournament(@PathVariable Long id, @RequestBody TournamentRequest request) {
        return AppResponse.success(tournamentService.updateTournament(id, request));
    }

    @GetMapping("/getTournamentInfo/{id}")
    public AppResponse<Tournament> getTournamentById(@PathVariable Long id) {
        return AppResponse.success(tournamentService.getTournamentById(id));
    }

    @Tag(name = ApiDocsTags.SEARCH)
    @GetMapping("/searchTournament")
    public AppResponse<List<ListTournamentRes>> getAllTournaments() {
        return AppResponse.success(tournamentService.getAllTournaments().stream().map(ListTournamentRes::from).toList());
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
