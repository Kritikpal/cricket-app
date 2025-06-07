package org.fastX;

import org.fastX.models.*;

import java.util.List;

public class PrintService {
    private final Match match;

    public PrintService(Match match) {
        this.match = match;
    }

    public void printMethod() {
        System.out.println(getMatchSummary());
    }

    public String getMatchSummary() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n=========== Match Summary ==========\n\n");

        sb.append("Match   : ")
                .append(match.getMatchInfo().getTeamA().getTeamName())
                .append(" vs ")
                .append(match.getMatchInfo().getTeamB().getTeamName())
                .append("\n");

        sb.append("Match status   : ")
                .append(match.getMatchStatus())
                .append("\n");

        if (match.getCurrentInnings() != null) {
            sb.append(getFormattedScoreboard(match.getBattingTeam(), match.getCurrentInnings()));
        } else {
            sb.append("First innings not available.\n");
        }

        sb.append("\n=========== End of Match ==========\n");

        return sb.toString();
    }

    private String getFormattedScoreboard(Team team, Innings innings) {
        if (innings == null) return "No innings data available.\n";

        Balls balls = innings.getBalls();
        Innings.ScoreCardState scoreCardState = innings.getScoreCardState();
        Score teamScore = balls.getScore();
        Player striker = innings.getStriker() != null ? innings.getStriker().getPlayer() : null;

        StringBuilder sb = new StringBuilder();

        sb.append("=== Scorecard for ").append(team != null ? team.getTeamName() : "Unknown Team").append(" ===\n\n");

        sb.append("Status   : ").append(scoreCardState != null ? scoreCardState : "N/A").append("\n");

        sb.append("\n--- Batting ---\n");
        sb.append(String.format("%-20s %5s %5s %5s %5s\n", "Name", "R", "B", "4s", "6s"));

        for (BatterInning batsman : innings.getBatterInnings()) {
            boolean isStriker = batsman.getPlayer().equals(striker);
            sb.append(getFormattedBatterStats(batsman, isStriker)).append("\n");
        }

        sb.append("\nScore     : ").append(teamScore != null ? teamScore.scoreSummary() : "N/A").append("\n");
        sb.append("Overs     : ").append(teamScore != null ? teamScore.overs() : "N/A").append("\n");

        if (innings.getCurrentBowler() != null) {
            sb.append("\nLast bowler: ").append(innings.getCurrentBowler().getPlayer().getFullName()).append("\n");
        }

        sb.append("\n---- Overs Summary ----\n");
        List<Over> overs = innings.getOvers();

        for (Over over : overs) {
            sb.append(over.getOverString());
        }


        sb.append("\n\n--- Bowling ---\n");
        sb.append(String.format("%-20s %5s %5s %5s %10s\n", "Name", "O", "R", "W", "Extras"));
        for (BowlerInning bowler : innings.getBowlerInnings()) {
            sb.append(getFormattedBowlerStats(bowler)).append("\n");
        }

        sb.append("\n--- Extras Breakdown ---\n");
        if (teamScore != null) {
            sb.append("Wides     : ").append(teamScore.getWides()).append("\n");
            sb.append("No Balls  : ").append(teamScore.getNoBalls()).append("\n");
            sb.append("Byes      : ").append(teamScore.getByes()).append("\n");
            sb.append("Leg Byes  : ").append(teamScore.getLegByes()).append("\n");
            sb.append("Total     : ").append(teamScore.totalExtras()).append("\n");
        }

        return sb.toString();
    }

    private String getFormattedBatterStats(BatterInning batterInning, boolean isStriker) {
        String name = batterInning.getPlayer() != null ? batterInning.getPlayer().getFullName() : "Unknown";
        if (isStriker) name += "*";

        String outStatus = "";
        if (batterInning.isOut() && batterInning.getDismissal() != null) {
            outStatus = " " + batterInning.getDismissal().getWord();
        }

        Score score = batterInning.getBalls().getScore();
        return String.format("%-20s %5d %5d %5d %5d  %8s",
                name,
                score.getBatterRuns(),
                score.getValidDeliveries(),
                score.getFours(),
                score.getSixes(),
                outStatus
        );
    }

    private String getFormattedBowlerStats(BowlerInning bowlerInning) {
        String name = bowlerInning.getPlayer() != null ? bowlerInning.getPlayer().getFullName() : "Unknown";
        Score score = bowlerInning.getBalls().getScore();

        return String.format("%-20s %5s %5d %5d %10d",
                name,
                score.overs(),
                score.bowlerRuns(),
                bowlerInning.getWicketsTaken(),
                score.totalExtras()
        );
    }
}
