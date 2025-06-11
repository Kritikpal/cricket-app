package com.fastx.live_score.adapter.admin.controller;

import com.fastx.live_score.adapter.admin.request.MatchRequest;
import com.fastx.live_score.domain.models.match.Match;
import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.domain.in.MatchService;
import com.fastx.live_score.core.utils.AppResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Match Admin")
@RequestMapping(APiConfig.API_VERSION_1 + "/match")
@RestController
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @ExampleObject(
                            """
                                    {
                                      "matchId":1,
                                      "tournamentId": 1,
                                      "teamAId": 1,
                                      "teamBId": 2,
                                      "startTime": 1746602788,
                                      "endTime": 1749281188
                                    }"""
                    )
            )
    )
    @PostMapping("/save")
    public AppResponse<String> createMatch(@RequestBody @Valid MatchRequest request) {
        matchService.saveMatch(request);
        return AppResponse.success("Match Created Successfully");
    }

    @GetMapping("/list")
    public AppResponse<List<Match>> allMatches() {
        return AppResponse.success(matchService.listMatches(null));
    }

    @GetMapping("/getMatchInfo/{matchId}")
    public AppResponse<Match> getMatchById(@PathVariable Long matchId) {
        return AppResponse.success(matchService.getMatchById(matchId));
    }

    @DeleteMapping("/delete/{matchId}")
    public AppResponse<String> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatch(matchId);
        return AppResponse.success("Deleted Successfully");
    }

}
