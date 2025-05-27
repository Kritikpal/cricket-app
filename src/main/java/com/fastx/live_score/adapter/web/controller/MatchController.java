package com.fastx.live_score.adapter.web.controller;

import com.fastx.live_score.adapter.web.request.CreateMatchRequest;
import com.fastx.live_score.adapter.web.response.MatchRes;
import com.fastx.live_score.infra.config.APiConfig;
import com.fastx.live_score.domain.in.MatchService;
import com.fastx.live_score.application.utils.AppResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "match", description = "All match apis")
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
    public AppResponse<MatchRes> createMatch(@RequestBody @Valid CreateMatchRequest request) {
        return AppResponse.success(matchService.saveMatch(request));
    }

    @PutMapping("/startMatch/{matchId}")
    public AppResponse<MatchRes> startMatch(@PathVariable Long matchId) {
        return AppResponse.success(matchService.startMatch(matchId));
    }

    @PutMapping("/endMatch/{matchId}")
    public AppResponse<MatchRes> endMatch(@PathVariable Long matchId, @RequestBody int winningTeam) {
        return AppResponse.success(matchService.endMatch(matchId,winningTeam));
    }


}
