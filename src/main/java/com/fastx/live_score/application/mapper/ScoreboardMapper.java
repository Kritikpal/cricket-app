package com.fastx.live_score.application.mapper;

import com.fastx.live_score.domain.Scoreboard;
import org.fastX.models.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardMapper {

    public static Scoreboard toDto(Match match) {
        Scoreboard dto = new Scoreboard();
        MatchInfo matchInfo = match.matchInfo();
        dto.matchTitle = matchInfo.teamA().teamName() + " vs " + matchInfo.teamB().teamName();
        dto.matchStatus = match.matchStatus().name();
        dto.teamA = matchInfo.teamA();
        dto.teamB = matchInfo.teamB();

        dto.inningsList = new ArrayList<>();

        List<Innings> inningsList = match.inningsList();
        if (inningsList != null) {
            for (Innings innings : inningsList) {
                Scoreboard.ScorecardInnings inningsDto = mapInnings(innings, match);
                dto.inningsList.add(inningsDto);
            }
        }

        return dto;
    }

    private static Scoreboard.ScorecardInnings mapInnings(Innings innings, Match match) {
        Scoreboard.ScorecardInnings inningsDto = new Scoreboard.ScorecardInnings();

        inningsDto.teamId = innings.team().teamId();

        inningsDto.inningsStatus = innings.scoreCardState();

        Balls balls = innings.balls();
        Score score = balls.score();

        // Team Score for this innings
        Scoreboard.TeamScore teamScore = new Scoreboard.TeamScore();
        teamScore.teamName = match.getBattingTeam().teamName();
        teamScore.scoreSummary = score.scoreSummary();
        teamScore.overs = score.overs();
        inningsDto.teamScore = teamScore;

        // Batting Stats
        Player striker = innings.striker() != null ? innings.striker().player() : null;
        inningsDto.battingStats = new ArrayList<>();
        for (BatterInning bi : innings.batterInnings()) {
            Score batterScore = bi.balls().score();
            Scoreboard.BatterStats bs = new Scoreboard.BatterStats();
            bs.name = bi.player().fullName();
            bs.runs = batterScore.batterRuns();
            bs.balls = batterScore.validDeliveries();
            bs.fours = batterScore.fours();
            bs.sixes = batterScore.sixes();
            bs.isStriker = bi.player().equals(striker);
            bs.dismissal = bi.isOut() && bi.dismissal() != null
                    ? bi.dismissal().getWord()
                    : "Not out";
            inningsDto.battingStats.add(bs);
        }

        // Bowling Stats
        inningsDto.bowlingStats = new ArrayList<>();
        for (BowlerInning bi : innings.bowlerInnings()) {
            Score bs = bi.balls().score();
            Scoreboard.BowlerStats bowler = new Scoreboard.BowlerStats();
            bowler.name = bi.player().fullName();
            bowler.overs = bs.overs();
            bowler.runs = bs.bowlerRuns();
            bowler.wickets = bi.wicketsTaken();
            bowler.extras = bs.totalExtras();
            inningsDto.bowlingStats.add(bowler);
        }

        // Overs Summary
        inningsDto.oversSummary = new ArrayList<>();
        for (Over over : innings.overs()) {
            inningsDto.oversSummary.add(over.getOverString());
        }

        // Extras
        Scoreboard.Extras ex = new Scoreboard.Extras();
        ex.wides = score.wides();
        ex.noBalls = score.noBalls();
        ex.byes = score.byes();
        ex.legByes = score.legByes();
        ex.total = score.totalExtras();
        inningsDto.extras = ex;

        return inningsDto;
    }
}
