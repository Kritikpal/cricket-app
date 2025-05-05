package org.fastX.models;

import lombok.Builder;
import lombok.Getter;
import org.fastX.exception.GameException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Builder(toBuilder = true)
public class Score {

    public static final Score EMPTY = new Score(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public static final Score DOT_BALL = new Score(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1);

    public static final Score SINGLE = new Score(1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

    public static final Score TWO = new Score(2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1);

    public static final Score THREE = new Score(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1);

    public static final Score FOUR = new Score(4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1);

    public static final Score SIX = new Score(6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1);

    public static final Score WIDE = new Score(0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public static final Score NO_BALL = new Score(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public static final Score BYE = new Score(0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1);

    public static final Score LEG_BYE = new Score(0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1);

    public static final Score WICKET = new Score(0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1);


    private final int batterRuns;
    private final int wides;
    private final int wideDeliveries;
    private final int noBalls;
    private final int legByes;
    private final int byes;
    private final int penaltyRuns;
    private final int wickets;
    private final int dots;
    private final int singles;
    private final int twos;
    private final int threes;
    private final int fours;
    private final int sixes;
    private final int validDeliveries;

    private Score(int batterRuns, int wides, int wideDeliveries, int noBalls, int legByes, int byes, int penaltyRuns, int wickets, int dots, int singles, int twos, int threes, int fours, int sixes, int validDeliveries) {
        this.batterRuns = batterRuns;
        this.wides = wides;
        this.wideDeliveries = wideDeliveries;
        this.noBalls = noBalls;
        this.legByes = legByes;
        this.byes = byes;
        this.penaltyRuns = penaltyRuns;
        this.wickets = wickets;
        this.dots = dots;
        this.singles = singles;
        this.twos = twos;
        this.threes = threes;
        this.fours = fours;
        this.sixes = sixes;
        this.validDeliveries = validDeliveries;
    }

    public Score add(Score other) {
        return this.toBuilder()
                .batterRuns(this.batterRuns + other.batterRuns)
                .wides(this.wides + other.wides)
                .wideDeliveries(this.wideDeliveries + other.wideDeliveries)
                .noBalls(this.noBalls + other.noBalls)
                .legByes(this.legByes + other.legByes)
                .byes(this.byes + other.byes)
                .penaltyRuns(this.penaltyRuns + other.penaltyRuns)
                .wickets(this.wickets + other.wickets)
                .dots(this.dots + other.dots)
                .singles(this.singles + other.singles)
                .twos(this.twos + other.twos)
                .threes(this.threes + other.threes)
                .fours(this.fours + other.fours)
                .sixes(this.sixes + other.sixes)
                .validDeliveries(this.validDeliveries + other.validDeliveries)
                .build();
    }

    // === Breakdown ===
    public int bowlerRuns() {
        return batterRuns + wides + noBalls + penaltyRuns;
    }

    public int fieldingExtras() {
        return byes + legByes + penaltyRuns;
    }

    public int bowlingExtras() {
        return wides + noBalls;
    }

    public int totalExtras() {
        return wides + noBalls + byes + legByes + penaltyRuns;
    }


    public int totalRunsConceded() {
        return bowlerRuns() + fieldingExtras();
    }

    // === Stats ===

    public int overCount() {
        return (validDeliveries / 6);
    }

    public int ballCount() {
        return (validDeliveries % 6);
    }

    public String overs() {
        return overCount() + "." + ballCount();
    }

    public double strikeRate() {
        return validDeliveries == 0 ? 0.0 : (batterRuns * 100.0) / validDeliveries;
    }

    public double getEconomy() {
        return validDeliveries == 0 ? 0.0 : (totalRunsConceded() * 6.0) / validDeliveries;
    }

    // === Booleans / Conditions ===


    public boolean isWicket() {
        return wickets > 0;
    }

    public boolean isBoundary() {
        return fours > 0 || sixes > 0;
    }

    public static Score parse(String text) {
        if (text == null || text.isBlank()) throw new GameException("Score input is blank or null", 400);

        if (".".equals(text)) return DOT_BALL;
        if ("W".equals(text)) return WICKET;

        Pattern pattern = Pattern.compile("^(?<num>\\d+)(?<modifier>[A-Za-z]+)?$");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.matches()) throw new GameException("Invalid score format: " + text, 400);

        int runs = Integer.parseInt(matcher.group("num"));
        String modifier = matcher.group("modifier");
        int batterRuns = runs;
        boolean dotBallIfNoRuns = true;

        Score.ScoreBuilder score = Score.EMPTY.toBuilder();

        if (modifier == null) {
            score.validDeliveries(1);
        } else {
            switch (modifier) {
                case "W":
                    score.validDeliveries(1).wickets(1);
                    break;
                case "w":
                    batterRuns = 0;
                    score.wides(runs);
                    dotBallIfNoRuns = false;
                    break;
                case "nb":
                    batterRuns = runs - 1;
                    score.noBalls(1);
                    dotBallIfNoRuns = false;
                    break;
                case "b":
                    batterRuns = 0;
                    score.byes(runs).validDeliveries(1);
                    break;
                case "lb":
                    batterRuns = 0;
                    score.legByes(runs).validDeliveries(1);
                    break;
                default:
                    throw new GameException("Unknown score modifier: " + modifier, 400);
            }
        }

        score.batterRuns(batterRuns);

        if (dotBallIfNoRuns && batterRuns == 0) score.dots(1);
        if (batterRuns == 1) score.singles(1);
        if (batterRuns == 2) score.twos(1);
        if (batterRuns == 3) score.threes(1);
        if (batterRuns == 4) score.fours(1);
        if (batterRuns == 6) score.sixes(1);

        return score.build();
    }

    public  String getLetter() {

        if (this.getWickets() == 1 && this.getValidDeliveries() == 1) {
            return "W";
        }

        if (this.getDots() == 1 && this.getValidDeliveries() == 1) {
            return ".";
        }

        int batterRuns = this.getBatterRuns();

        if (this.getNoBalls() == 1) {
            return (batterRuns + 1) + "nb";  // Total runs scored on no ball
        }

        if (this.getWides() > 0) {
            return this.getWides() + "w";
        }

        if (this.getByes() > 0) {
            return this.getByes() + "b";
        }

        if (this.getLegByes() > 0) {
            return this.getLegByes() + "lb";
        }

        if (this.getValidDeliveries() == 1) {
            return String.valueOf(batterRuns);
        }

        throw new GameException("Cannot stringify unknown score pattern", 400);
    }

    public String scoreSummary() {
        return totalRunsConceded() + "/" + wickets;
    }

}
