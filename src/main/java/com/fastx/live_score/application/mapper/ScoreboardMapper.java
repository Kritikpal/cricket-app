package com.fastx.live_score.application.mapper;

import com.fastx.live_score.domain.Scoreboard;
import org.fastX.models.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardMapper {

    public static Scoreboard toDto(Match match) {
        Scoreboard dto = new Scoreboard();
        MatchInfo matchInfo = match.getMatchInfo();
        dto.matchTitle = matchInfo.getTeamA().getTeamName() + " vs " + matchInfo.getTeamB().getTeamName();
        dto.matchStatus = match.getMatchStatus().name();
        dto.teamA = matchInfo.getTeamA();
        dto.teamB = matchInfo.getTeamB();

        dto.inningsList = new ArrayList<>();

        List<Innings> inningsList = match.getInningsList();
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

        inningsDto.teamId = innings.getTeam().getTeamId();

        inningsDto.inningsStatus = innings.getScoreCardState();

        Balls balls = innings.getBalls();
        Score score = balls.getScore();

        // Team Score for this innings
        Scoreboard.TeamScore teamScore = new Scoreboard.TeamScore();
        teamScore.teamName = match.getBattingTeam().getTeamName();
        teamScore.scoreSummary = score.scoreSummary();
        teamScore.overs = score.overs();
        inningsDto.teamScore = teamScore;

        // Batting Stats
        Player striker = innings.getStriker() != null ? innings.getStriker().getPlayer() : null;
        inningsDto.battingStats = new ArrayList<>();
        for (BatterInning bi : innings.getBatterInnings()) {
            Score batterScore = bi.getBalls().getScore();
            Scoreboard.BatterStats bs = new Scoreboard.BatterStats();
            bs.name = bi.getPlayer().getFullName();
            bs.runs = batterScore.getBatterRuns();
            bs.balls = batterScore.getValidDeliveries();
            bs.fours = batterScore.getFours();
            bs.sixes = batterScore.getSixes();
            bs.isStriker = bi.getPlayer().equals(striker);
            bs.dismissal = bi.isOut() && bi.getDismissal() != null
                    ? bi.getDismissal().getWord()
                    : "Not out";
            inningsDto.battingStats.add(bs);
        }

        // Bowling Stats
        inningsDto.bowlingStats = new ArrayList<>();
        for (BowlerInning bi : innings.getBowlerInnings()) {
            Score bs = bi.getBalls().getScore();
            Scoreboard.BowlerStats bowler = new Scoreboard.BowlerStats();
            bowler.name = bi.getPlayer().getFullName();
            bowler.overs = bs.overs();
            bowler.runs = bs.bowlerRuns();
            bowler.wickets = bi.getWicketsTaken();
            bowler.extras = bs.totalExtras();
            inningsDto.bowlingStats.add(bowler);
        }

        // Overs Summary
        inningsDto.oversSummary = new ArrayList<>();
        for (Over over : innings.getOvers()) {
            inningsDto.oversSummary.add(over.getOverString());
        }

        // Extras
        Scoreboard.Extras ex = new Scoreboard.Extras();
        ex.wides = score.getWides();
        ex.noBalls = score.getNoBalls();
        ex.byes = score.getByes();
        ex.legByes = score.getLegByes();
        ex.total = score.totalExtras();
        inningsDto.extras = ex;

        return inningsDto;
    }
}
