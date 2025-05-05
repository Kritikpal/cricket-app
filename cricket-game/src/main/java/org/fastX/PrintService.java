package org.fastX;

import org.fastX.models.*;
import org.fastX.models.events.BallCompleteEvent;

import java.util.List;

public class PrintService {
    private final Match match;

    public PrintService(Match match) {
        this.match = match;
    }

    public void printMethod() {
        System.out.println(getFormattedScoreboard(match.getCurrentInnings()));
    }

    private String getFormattedScoreboard(Innings innings) {
        Balls balls = innings.getBalls();
        Team team = innings.getTeam();
        Innings.ScoreCardState scoreCardState = innings.getScoreCardState();

        Score teamScore = balls.getScore();
        StringBuilder sb = new StringBuilder();

        Player striker = innings.getStriker() != null ? innings.getStriker().getPlayer() : null;

        sb.append("=== Scorecard for ").append(team.getTeamName()).append(" ===\n\n");

        sb.append("Status   : ").append(scoreCardState).append("\n");

        sb.append("\n--- Batting ---\n");
        sb.append(String.format("%-20s %5s %5s %5s %5s\n", "Name", "R", "B", "4s", "6s"));

        for (BatterInning batsman : innings.getBatterInnings()) {
            boolean isStriker = batsman.getPlayer().equals(striker);
            sb.append(getFormattedBatterStats(batsman, isStriker)).append("\n");
        }

        sb.append("\nScore     : ").append(teamScore.scoreSummary()).append("\n");
        sb.append("Overs     : ").append(teamScore.overs()).append("\n");

        if (innings.getCurrentBowler() != null) {
            sb.append("\n");
            sb.append("Last bowler:").append(innings.getCurrentBowler().getPlayer().getFullName()).append("\n");
        }

        int eventCount = balls.size();
        List<BallCompleteEvent> last10Events = balls.getBalls().subList(0, eventCount);
        sb.append("\n").append("----last balls----").append("\n");
        for (int i = 0; i < balls.size(); i++) {
            BallCompleteEvent event = last10Events.get(i);
            sb.append(String.format("%s\t", event.getRunScored().getLetter()));
        }


        sb.append("\n--- Bowling ---\n");
        sb.append(String.format("%-20s %5s %5s %5s %10s\n", "Name", "O", "R", "W", "Extras"));
        for (BowlerInning bowler : innings.getBowlerInnings()) {
            sb.append(getFormattedBowlerStats(bowler)).append("\n");
        }


        sb.append("\n--- Extras Breakdown ---\n");
        sb.append("Wides     : ").append(balls.getScore().getWides()).append("\n");
        sb.append("No Balls  : ").append(balls.getScore().getNoBalls()).append("\n");
        sb.append("Byes  : ").append(balls.getScore().getByes()).append("\n");
        sb.append("Lage byes  : ").append(balls.getScore().getLegByes()).append("\n");
        sb.append("Total     : ").append(balls.getScore().totalExtras()).append("\n");


        return sb.toString();
    }

    private String getFormattedBowlerStats(BowlerInning bowlerInning) {
        return String.format("%-20s %5s %5d %5d %10d",
                bowlerInning.getPlayer().getFullName(),
                bowlerInning.getBalls().getScore().overs(),
                bowlerInning.getBalls().getScore().bowlerRuns(),
                bowlerInning.getWicketsTaken(),
                bowlerInning.getBalls().getScore().totalExtras()
        );
    }

    private String getFormattedBatterStats(BatterInning batterInning, boolean isStriker) {
        String name = batterInning.getPlayer().getFullName();
        if (isStriker) name += "*";

        String outStatus = "";
        if (batterInning.isOut()) {
            outStatus += " " + batterInning.getDismissal().getWord();
        }
        return String.format("%-20s %5d %5d %5d %5d  %8s",
                name,
                batterInning.getBalls().getScore().getBatterRuns(),
                batterInning.getBalls().getScore().getValidDeliveries(),
                batterInning.getBalls().getScore().getFours(),
                batterInning.getBalls().getScore().getSixes(),
                outStatus
        );
    }
}
