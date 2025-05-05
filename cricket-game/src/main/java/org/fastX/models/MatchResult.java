package org.fastX.models;

import lombok.Data;

import static java.util.Objects.requireNonNull;

@Data
public class MatchResult {

    /**
     * Describes how a match was won, drawn, tied or otherwise concluded
     */
    public enum ResultType {WON, TIED, DRAWN, NO_RESULT, ABANDONED, AWARDED, CONCEDED}

    /**
     * Specifies the measure used to determine the winning team
     */
    public enum Measure {
        WICKETS(false, "wicket", "wickets"),
        RUNS(false, "run", "runs"),
        INNINGS_AND_RUNS(false, "run", "runs"),
        WICKET_COUNT(true, "in wicket count (tie breaker)", null),
        BOUNDARY_COUNT(true, "boundaries in boundary count (tie breaker)", null),
        WICKETS_IN_BOWL_OUT(true, "wickets in bowl out (tie breaker)", null);

        private final boolean isTieBreaker;
        private final String singular;
        private final String plural;

        Measure(boolean isTieBreaker, String singular, String plural) {
            this.isTieBreaker = isTieBreaker;
            this.singular = singular;
            this.plural = plural;
        }

        public String toString(int count) {
            String prefix = (this == INNINGS_AND_RUNS) ? "an innings and " : "";
            return prefix + count + " " + ((count == 1) ? singular : requireNonNull(plural, singular));
        }

        /**
         * @return True if the match was tied and then the winner was decided on some other measure
         */
        public boolean tieBreakerUsed() {
            return isTieBreaker;
        }
    }

    private final ResultType resultType;
    private final Team winningTeam;
    private final Measure wonBy;
    private final Integer wonByAmount;
    private final boolean duckworthLewisApplied;

    @Override
    public String toString() {
        return switch (resultType) {
            case NO_RESULT -> "No result";
            case AWARDED -> "Awarded to " + winningTeam.getTeamId();
            case CONCEDED -> "Conceded. Won by " + winningTeam.getTeamName();
            case WON -> winningTeam.getTeamName() + " won by " + wonBy.toString(wonByAmount);
            case TIED -> "Match tied";
            default -> resultType.toString();
        };
    }
}
