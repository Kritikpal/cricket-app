package com.fastx.live_score.adapter.ball_user;

import com.fastx.live_score.adapter.ball_user.req.AddScoreRequest;
import com.fastx.live_score.adapter.ball_user.req.StartInningsRequest;
import com.fastx.live_score.adapter.ball_user.req.StartMatchRequest;
import com.fastx.live_score.core.config.APiConfig;
import com.fastx.live_score.core.utils.AppResponse;
import com.fastx.live_score.domain.Scoreboard;
import com.fastx.live_score.domain.in.MatchEventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.fastX.models.Match;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(APiConfig.API_VERSION_1 + "/match/live/{id}")
@Tag(name = "Ball User Api")
public class BallUserController {

    private final MatchEventService matchEventService;

    public BallUserController(MatchEventService matchEventService) {
        this.matchEventService = matchEventService;
    }

    @PutMapping("/start")
    public AppResponse<Match> startMatch(@PathVariable Long id, @Valid @RequestBody StartMatchRequest startMatchRequest) throws IOException, ClassNotFoundException {
        return AppResponse.success(matchEventService.startMatch(id, startMatchRequest.teamA(), startMatchRequest.teamB()));
    }

    @PutMapping("/startInnings")
    public AppResponse<Scoreboard> startInning(@PathVariable Long id, @Valid @RequestBody StartInningsRequest request) {
        return AppResponse.success(
                matchEventService.startInnings(id, request.getTeamId(), request.getStriker(), request.getNonStriker(), request.getBowlerId())
        );
    }

    @PutMapping("/startOver/{bowlerId}")
    public AppResponse<Match> startOver(@PathVariable Long id, @PathVariable Long bowlerId) {
        return AppResponse.success(matchEventService.startOver(id, bowlerId));
    }

    @PutMapping("/addScore")
    public AppResponse<Scoreboard> addScore(@PathVariable Long id, @RequestBody AddScoreRequest score) {
        return AppResponse.success(matchEventService.addBallEvent(id, score.getScore()));
    }

    @GetMapping("/scoreBoard")
    public String getScoreBoard(@PathVariable long id) {
        return matchEventService.getScoreBoard(id);
    }

    @GetMapping("/getScoreCard")
    public AppResponse<Scoreboard> getScoreCard(@PathVariable long id) {
        return AppResponse.success(matchEventService.getLiveMatchStatus(id));
    }
}
