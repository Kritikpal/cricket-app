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
                .append(match.matchInfo().teamA().teamName())
                .append(" vs ")
                .append(match.matchInfo().teamB().teamName())
                .append("\n");

        sb.append("Match status   : ")
                .append(match.matchStatus())
                .append("\n");

        if (match.currentInnings() != null) {
            sb.append(getFormattedScoreboard(match.getBattingTeam(), match.currentInnings()));
        } else {
            sb.append("First innings not available.\n");
        }

        sb.append("\n=========== End of Match ==========\n");

        return sb.toString();
    }

    private String getFormattedScoreboard(Team team, Innings innings) {
        if (innings == null) return "No innings data available.\n";

        Balls balls = innings.balls();
        Innings.ScoreCardState scoreCardState = innings.scoreCardState();
        Score teamScore = balls.score();
        Player striker = innings.striker() != null ? innings.striker().player() : null;

        StringBuilder sb = new StringBuilder();

        sb.append("=== Scorecard for ").append(team != null ? team.teamName() : "Unknown Team").append(" ===\n\n");

        sb.append("Status   : ").append(scoreCardState != null ? scoreCardState : "N/A").append("\n");

        sb.append("\n--- Batting ---\n");
        sb.append(String.format("%-20s %5s %5s %5s %5s\n", "Name", "R", "B", "4s", "6s"));

        for (BatterInning batsman : innings.batterInnings()) {
            boolean isStriker = batsman.player().equals(striker);
            sb.append(getFormattedBatterStats(batsman, isStriker)).append("\n");
        }

        sb.append("\nScore     : ").append(teamScore != null ? teamScore.scoreSummary() : "N/A").append("\n");
        sb.append("Overs     : ").append(teamScore != null ? teamScore.overs() : "N/A").append("\n");

        if (innings.currentBowler() != null) {
            sb.append("\nLast bowler: ").append(innings.currentBowler().player().fullName()).append("\n");
        }

        sb.append("\n---- Overs Summary ----\n");
        List<Over> overs = innings.overs();

        for (Over over : overs) {
            sb.append(over.getOverString());
        }


        sb.append("\n\n--- Bowling ---\n");
        sb.append(String.format("%-20s %5s %5s %5s %10s\n", "Name", "O", "R", "W", "Extras"));
        for (BowlerInning bowler : innings.bowlerInnings()) {
            sb.append(getFormattedBowlerStats(bowler)).append("\n");
        }

        sb.append("\n--- Extras Breakdown ---\n");
        if (teamScore != null) {
            sb.append("Wides     : ").append(teamScore.wides()).append("\n");
            sb.append("No Balls  : ").append(teamScore.noBalls()).append("\n");
            sb.append("Byes      : ").append(teamScore.byes()).append("\n");
            sb.append("Leg Byes  : ").append(teamScore.legByes()).append("\n");
            sb.append("Total     : ").append(teamScore.totalExtras()).append("\n");
        }

        return sb.toString();
    }

    private String getFormattedBatterStats(BatterInning batterInning, boolean isStriker) {
        String name = batterInning.player() != null ? batterInning.player().fullName() : "Unknown";
        if (isStriker) name += "*";

        String outStatus = "";
        if (batterInning.isOut() && batterInning.dismissal() != null) {
            outStatus = " " + batterInning.dismissal().getWord();
        }

        Score score = batterInning.balls().score();
        return String.format("%-20s %5d %5d %5d %5d  %8s",
                name,
                score.batterRuns(),
                score.validDeliveries(),
                score.fours(),
                score.sixes(),
                outStatus
        );
    }

    private String getFormattedBowlerStats(BowlerInning bowlerInning) {
        String name = bowlerInning.player() != null ? bowlerInning.player().fullName() : "Unknown";
        Score score = bowlerInning.balls().score();

        return String.format("%-20s %5s %5d %5d %10d",
                name,
                score.overs(),
                score.bowlerRuns(),
                bowlerInning.wicketsTaken(),
                score.totalExtras()
        );
    }
}
